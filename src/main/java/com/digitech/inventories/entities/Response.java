package com.digitech.inventories.entities;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class Response {
    private LocalDateTime responseAt;
    private String message;

    public LocalDateTime getResponseAt() {
        return responseAt;
    }

    public void setResponseAt(LocalDateTime responseAt) {
        this.responseAt = responseAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
