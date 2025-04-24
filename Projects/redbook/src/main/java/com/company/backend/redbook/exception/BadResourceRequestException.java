package com.company.backend.redbook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadResourceRequestException extends RuntimeException {
    private HttpStatus httpStatus;
    private String message;

    public BadResourceRequestException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public BadResourceRequestException(String message, HttpStatus httpStatus, String alternativeMessage) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = alternativeMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
