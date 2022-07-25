package com.github.tozymc.pluginloader;

public class InjectionInstanceException extends Exception {
  public InjectionInstanceException() {
  }

  public InjectionInstanceException(String message) {
    super(message);
  }

  public InjectionInstanceException(String message, Throwable cause) {
    super(message, cause);
  }
}
