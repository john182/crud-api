package com.crud.user.exceptionhandler;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ApiValidationErro extends ApiSubErro {

    private String object;
    private String field;
    private Object valueRejected;
    private String message;

    public ApiValidationErro(String object, String message) {
        this.object = object;
        this.message = message;
    }

    public ApiValidationErro(String object, String campo, Object valueRejected, String message) {
        this.object = object;
        this.field = field;
        this.valueRejected = valueRejected;
        this.message = message;
    }
}
