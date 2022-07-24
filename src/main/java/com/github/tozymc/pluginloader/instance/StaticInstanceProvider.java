package com.github.tozymc.pluginloader.instance;

import com.github.tozymc.pluginloader.PluginContainer;
import org.jetbrains.annotations.NotNull;

final class StaticInstanceProvider<T> implements InstanceProvider<T> {
  private final T instance;

  StaticInstanceProvider(T instance) {
    this.instance = instance;
  }

  @Override
  public @NotNull T get(@NotNull PluginContainer container) {
    return instance;
  }
}
