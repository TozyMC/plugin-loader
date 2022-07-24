package com.github.tozymc.pluginloader;

import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;

public interface PluginProvider {
  @NotNull PluginInfo loadCandidate(@NotNull Path source);

  @NotNull PluginInfo createFromCandidate(@NotNull PluginInfo candidate);

  void create(@NotNull PluginContainer container);
}
