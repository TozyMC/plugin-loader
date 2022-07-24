package com.github.tozymc.pluginloader;

import java.nio.file.Path;
import java.util.Optional;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;

public interface PluginInfo {
  java.util.regex.Pattern ID_PATTERN = java.util.regex.Pattern.compile("[a-z][a-z0-9-_]{0,63}");

  @Pattern("[a-z][a-z0-9-_]{0,63}")
  @NotNull String id();

  @NotNull Path source();

  @NotNull String mainClass();

  @NotNull Optional<String> name();

  @NotNull Optional<String> version();

  @NotNull Optional<String> description();

  @NotNull Iterable<String> authors();

  @NotNull Iterable<Dependency> dependencies();

  @NotNull Libraries libraries();

  interface Dependency {
    @Pattern("[a-z][a-z0-9-_]{0,63}")
    @NotNull String id();

    boolean optional();
  }

  interface Libraries {
    java.util.regex.Pattern COORDINATE_PATTERN = java.util.regex.Pattern.compile(
        "([^: ]+):([^: ]+)(:([^: ]*)(:([^: ]+))?)?:([^: ]+)");

    @NotNull Iterable<String> repositories();

    @NotNull Iterable<String> artifacts();
  }
}
