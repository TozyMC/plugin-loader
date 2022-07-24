package com.github.tozymc.pluginloader.instance;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface InstanceProviderRegistry {
  <T> void register(@NotNull Class<T> type, @Nullable String name, InstanceProvider<T> creator);

  default <T> void register(@NotNull Class<T> type, InstanceProvider<T> creator) {
    register(type, null, creator);
  }

  <T> InstanceProvider<T> get(@NotNull Class<T> type, @Nullable String name);

  default <T> InstanceProvider<T> get(@NotNull Class<T> type) {
    return get(type, null);
  }
}
