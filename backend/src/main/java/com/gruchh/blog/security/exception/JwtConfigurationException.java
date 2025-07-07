package com.gruchh.blog.security.exception;

public class JwtConfigurationException extends RuntimeException {
  public JwtConfigurationException(String message) {
    super(message);
  }
}