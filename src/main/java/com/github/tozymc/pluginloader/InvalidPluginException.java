package com.github.tozymc.pluginloader;

public class InvalidPluginException extends Exception {
  public InvalidPluginException() {
    super();
  }

  public InvalidPluginException(String message) {
    super(message);
  }

  public InvalidPluginException(String message, Throwable cause) {
    super(message, cause);
  }
}
