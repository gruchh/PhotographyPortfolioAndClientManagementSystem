package com.gruchh.blog.shared.exception;

public class JwtTokenExpiredException extends JwtException {
    public JwtTokenExpiredException(String message) {
        super(message);
    }
}