package com.github.tozymc.pluginloader.internal;

import com.github.tozymc.pluginloader.instance.InstanceProvider;
import com.github.tozymc.pluginloader.instance.InstanceProviderRegistry;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class JavaInstanceProviderRegistry implements InstanceProviderRegistry {
  private final Map<Class<?>, InstanceProviderMap<?>> providerMap = new HashMap<>();

  @Override
  public <T> void register(@NotNull Class<T> type, @Nullable String name,
      InstanceProvider<T> creator) {
    var subMap = providerMap.get(type);
    if (subMap == null) {
      synchronized (providerMap) {
        var newMap = new InstanceProviderMap<T>();
        providerMap.put(type, newMap);
        subMap = newMap;
      }
    }
    subMap.put(name, creator);
  }

  @Override
  public <T> InstanceProvider<T> get(@NotNull Class<T> type, @Nullable String name) {
    var subMap = providerMap.get(type);
    if (subMap == null) {
      return null;
    }
    //noinspection unchecked
    return (InstanceProvider<T>) subMap.get(name);
  }

  private static final class InstanceProviderMap<T> extends HashMap<String, InstanceProvider<T>> {
    private void put(String name, InstanceProvider<?> creator) {
      //noinspection unchecked
      super.put(name, (InstanceProvider<T>) creator);
    }
  }
}
