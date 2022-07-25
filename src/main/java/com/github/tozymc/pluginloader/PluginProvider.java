package com.github.tozymc.pluginloader;

import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public interface PluginProvider {
  @NotNull PluginInfo loadCandidate(@NotNull Path source) throws InvalidPluginException;

  @NotNull PluginInfo createFromCandidate(@NotNull PluginInfo candidate, List<URL> libraryUrls)
      throws ClassNotFoundException;

  void create(@NotNull PluginContainer container)
      throws PluginInitializationException, InjectionInstanceException;
}
