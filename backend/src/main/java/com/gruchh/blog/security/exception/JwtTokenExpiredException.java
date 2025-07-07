package com.gruchh.blog.security.exception;

public class JwtTokenExpiredException extends JwtException {
    public JwtTokenExpiredException(String message) {
        super(message);
    }
}