package com.rkey.rkms_backend.core.api;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ResponseType {
    ACCOUNT_CREATED("auth.account_created", HttpStatus.CREATED),
    LOGIN_SUCCESS("auth.login_success", HttpStatus.OK),
    UNAUTHORIZED("auth.unauthorized", HttpStatus.UNAUTHORIZED);

    private final String code;
    private final HttpStatus httpStatus;

    ResponseType(String code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }
}