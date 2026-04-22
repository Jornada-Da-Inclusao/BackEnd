package com.fatec.exception;

public enum ErrorCode {
    USER_NOT_FOUND("USR_404"),
    USER_ALREADY_EXISTS("USR_409"),

    TOKEN_EMAIL_NOT_FOUND("TKEM_404"),
    TOKEN_EMAIL_UNAUTHORIZED("TKEM_401"),
    INVALID_CREDENTIALS("AUTH_401"),

    INVALID_REQUEST("REQ_400"),

    DEPENDENTE_NOT_FOUND("DEP_404"),
    RESOURCE_NOT_FOUND("RES_404"),

    INTERNAL_ERROR("SYS_500");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}