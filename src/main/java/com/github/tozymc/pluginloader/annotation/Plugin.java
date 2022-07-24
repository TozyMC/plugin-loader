package com.github.tozymc.pluginloader.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Plugin {
  @Pattern("[a-z][a-z0-9-_]{0,63}") @NotNull String id();

  String name() default "";

  String version() default "";

  String description() default "";

  String[] authors() default {};

  Dependency[] dependencies() default {};

  Libraries libraries() default @Libraries;
}
