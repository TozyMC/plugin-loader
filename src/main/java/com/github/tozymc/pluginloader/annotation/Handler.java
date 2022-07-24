package com.github.tozymc.pluginloader.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.NotNull;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Handler {
  @NotNull Type type();

  enum Type {ON_INITIALIZED, ON_ENABLED, ON_DISABLED}
}
