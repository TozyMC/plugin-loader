package com.github.tozymc.pluginloader;

import java.net.URL;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface PluginContainer {
  @NotNull PluginInfo info();

  @NotNull Optional<?> plugin();

  void addToClassPath(@NotNull URL url);
}
