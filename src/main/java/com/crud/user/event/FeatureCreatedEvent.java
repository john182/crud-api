package com.crud.user.event;

import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletResponse;

public class FeatureCreatedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1l;


    private HttpServletResponse response;
    private Integer id;

    public FeatureCreatedEvent(Object source, HttpServletResponse response, Integer id) {
        super(source);
        this.response = response;
        this.id = id;
    }


    public HttpServletResponse getResponse() {
        return response;
    }

    public Integer getId() {
        return id;
    }
}
