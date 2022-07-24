package com.github.tozymc.pluginloader.instance;

import com.github.tozymc.pluginloader.PluginContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface InstanceRegistry {
  void registerStatic(@NotNull Class<?> type, @Nullable String name, @NotNull Object instance);

  default void registerStatic(@NotNull Class<?> type, @NotNull Object instance) {
    registerStatic(type, null, instance);
  }

  <T> void register(@NotNull Class<T> type, @Nullable String name, InstanceCreator<T> creator);

  <T> void register(@NotNull Class<T> type, InstanceCreator<T> creator);

  <T> T getStatic(@NotNull Class<T> type, @Nullable String name);

  default <T> T getStatic(@NotNull Class<T> type) {
    return getStatic(type, null);
  }

  <T> T get(@NotNull Class<T> type, @NotNull PluginContainer container, @Nullable String name);

  default <T> T get(@NotNull Class<T> type, @NotNull PluginContainer container) {
    return get(type, container, null);
  }
}
