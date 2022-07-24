package com.github.tozymc.pluginloader.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Libraries {
  String[] repositories() default {};

  String[] artifacts() default {};
}
