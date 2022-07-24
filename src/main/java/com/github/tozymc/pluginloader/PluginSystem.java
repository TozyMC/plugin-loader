package com.github.tozymc.pluginloader;

import com.github.tozymc.pluginloader.instance.InstanceRegistry;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

public interface PluginSystem {
  void loadPlugins();

  boolean enablePlugin(PluginContainer container);

  boolean disablePlugin(PluginContainer container);

  void enablePlugins();

  void disablePlugins();

  @NotNull Optional<PluginContainer> plugin(@NotNull Object plugin);

  @NotNull Optional<PluginContainer> plugin(@NotNull String id);

  @NotNull @UnmodifiableView Collection<PluginContainer> plugins();

  @NotNull InstanceRegistry instanceRegistry();

  @NotNull Path pluginsDirectory();
}
