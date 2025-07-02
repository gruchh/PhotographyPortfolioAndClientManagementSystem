package com.gruchh.blog.shared.exception;

public class JwtConfigurationException extends RuntimeException {
  public JwtConfigurationException(String message) {
    super(message);
  }
}