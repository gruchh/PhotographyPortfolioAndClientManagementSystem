package com.gruchh.blog.security.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException {
        private final HttpStatus status;

        public ValidationException(String message, HttpStatus status) {
            super(message);
            this.status = status;
        }

        public HttpStatus getStatus() {
            return status;
        }
    }
