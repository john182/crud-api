package com.crud.user.exceptionhandler;


import com.crud.user.service.exception.BusinessException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;
import java.util.StringJoiner;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class CrudExceptionHandler extends ResponseEntityExceptionHandler {


    private static final String NO_MESSSAGE_AVAILABLE = "No message available";
    private static final Logger LOG = LoggerFactory.getLogger(CrudExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;


    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String error = ex.getParameterName() + "the parameter is missing";
        return super.handleMissingServletRequestParameter(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseEntity(new ApiErro(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
    }

    /**
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiErro apiErro = new ApiErro(BAD_REQUEST);
        apiErro.setMessage("Erro de validação");
        apiErro.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiErro.addValidationError(ex.getBindingResult().getGlobalErrors());

        return buildResponseEntity(apiErro);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException exception
            , Locale locale) {
        ApiErro apiErro = new ApiErro(BAD_REQUEST);
        apiErro.setMessage("JSON invalid");
        apiErro.setDebugMessage(buildMessageForInvalidFormat(exception));

        return buildResponseEntity(apiErro);
    }

    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(
            javax.validation.ConstraintViolationException ex) {
        ApiErro apiErro = new ApiErro(BAD_REQUEST);
        apiErro.setMessage("Error validator");
        apiErro.addValidationErrors(ex.getConstraintViolations());
        return buildResponseEntity(apiErro);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
        ApiErro apiErro = new ApiErro(NOT_FOUND);
        apiErro.setMessage(messageSource.getMessage("feature not create", null, LocaleContextHolder.getLocale()));
        apiErro.setDebugMessage(ex.toString());

        return buildResponseEntity(apiErro);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            EntityNotFoundException ex) {
        ApiErro apiErro = new ApiErro(NOT_FOUND);
        apiErro.setMessage(ex.getMessage());
        return buildResponseEntity(apiErro);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        //log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        String error = "Malformed JSON request\n";
        return buildResponseEntity(new ApiErro(HttpStatus.BAD_REQUEST, error, ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Error writing JSON output";
        return buildResponseEntity(new ApiErro(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
    }

    /**
     * Handle javax.persistence.EntityNotFoundException
     */
    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
        return buildResponseEntity(new ApiErro(HttpStatus.NOT_FOUND, ex));
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {
        ApiErro apiError = new ApiErro(BAD_REQUEST);
        apiError.setMessage(String.format("Parameter '% s' of value '% s' can not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {

        if (ex.getCause() instanceof ConstraintViolationException) {
            return buildResponseEntity(new ApiErro(HttpStatus.CONFLICT, "Database error", ex.getCause().getCause()));
        }
        return buildResponseEntity(new ApiErro(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException exception, Locale locale) {
        return buildResponseEntity(new ApiErro(exception.getStatus(), exception.getMessage(), exception));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handlerInternalServerError(Exception exception, Locale locale) {
        LOG.error("Error not expected", exception);
        return buildResponseEntity(new ApiErro(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception));
    }


    private ResponseEntity<Object> buildResponseEntity(ApiErro apiErro) {

        return new ResponseEntity<>(apiErro, apiErro.getStatus());
    }


    private String buildMessageForInvalidFormat(InvalidFormatException ife) {


        StringBuilder msg = new StringBuilder("Invalid value: " + ife.getValue());

        Class<?> targetType = ife.getTargetType();
        if (targetType.isEnum()) {
            StringJoiner joiner = new StringJoiner(", ", ". Possible values: [", "]");

            Enum[] constants = (Enum[]) ife.getTargetType().getEnumConstants();
            for (Enum constant : constants) {
                joiner.add(constant.name());
            }

            msg.append(joiner.toString());
        }
        return msg.toString();
    }
}
