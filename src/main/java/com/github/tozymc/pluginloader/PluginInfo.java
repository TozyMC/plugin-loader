package com.github.tozymc.pluginloader;

import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

public interface PluginInfo {
  Pattern ID_PATTERN = Pattern.compile("[a-z][a-z0-9-_]{0,63}");

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
    @NotNull String id();

    boolean optional();
  }

  interface Libraries {
    Pattern COORDINATE_PATTERN =
        Pattern.compile("([^: ]+):([^: ]+)(:([^: ]*)(:([^: ]+))?)?:([^: ]+)");

    @NotNull Iterable<String> repositories();

    @NotNull Iterable<String> artifacts();
  }
}
