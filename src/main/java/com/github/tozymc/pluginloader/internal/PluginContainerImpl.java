package com.github.tozymc.pluginloader.internal;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import com.github.tozymc.pluginloader.PluginContainer;
import java.net.URL;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

class PluginContainerImpl implements PluginContainer {
  private final LoadedPluginInfo pluginInfo;
  private Object plugin;

  public PluginContainerImpl(LoadedPluginInfo pluginInfo) {
    this.pluginInfo = requireNonNull(pluginInfo, "pluginInfo");
  }

  @Override
  public @NotNull LoadedPluginInfo info() {
    return pluginInfo;
  }

  @Override
  public @NotNull Optional<?> plugin() {
    return ofNullable(plugin);
  }

  Object pluginInstance() {
    return requireNonNull(plugin, "plugin");
  }

  @Override
  public void addToClassPath(@NotNull URL url) {
    if (pluginInfo.loadedMainClass().getClassLoader() instanceof PluginClassLoader pc) {
      pc.addToClasspath(url);
    }
  }

  public void plugin(Object plugin) {
    this.plugin = plugin;
  }
}
