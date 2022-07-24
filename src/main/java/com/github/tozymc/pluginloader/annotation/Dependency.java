package com.github.tozymc.pluginloader.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Dependency {
  @Pattern("[a-z][a-z0-9-_]{0,63}") @NotNull String id();

  boolean optional() default false;
}
