package com.github.tozymc.pluginloader.instance;

import com.github.tozymc.pluginloader.PluginContainer;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface InstanceCreator<T> {
  @NotNull T create(@NotNull PluginContainer container);
}
