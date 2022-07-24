module com.github.tozymc.pluginloader {
  requires static transitive org.jetbrains.annotations;

  requires org.slf4j;
  requires com.google.common;

  exports com.github.tozymc.pluginloader;
  exports com.github.tozymc.pluginloader.annotation;
  exports com.github.tozymc.pluginloader.instance;
}