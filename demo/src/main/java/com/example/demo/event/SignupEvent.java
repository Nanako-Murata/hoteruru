package com.example.demo.event;

import org.springframework.context.ApplicationEvent;

import com.example.demo.entity.User;

import lombok.Getter;

@Getter
public class SignupEvent extends ApplicationEvent {
    private User user;
    private String requestUrl;

    public SignupEvent(Object source, User user, String requetUrl) {
        super(source);
        this.user = user;
        this.requestUrl = requetUrl;
    }

}
