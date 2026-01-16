package com.company.platform.api.dto;

import java.time.LocalDateTime;

public class ApiErrorResponse {

    private String status;
    private String message;
    private LocalDateTime timestamp;

    public ApiErrorResponse(String status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
