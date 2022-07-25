package com.github.tozymc.pluginloader;

public class PluginInitializationException extends Exception {
  public PluginInitializationException() {
  }

  public PluginInitializationException(String message) {
    super(message);
  }

  public PluginInitializationException(String message, Throwable cause) {
    super(message, cause);
  }
}
