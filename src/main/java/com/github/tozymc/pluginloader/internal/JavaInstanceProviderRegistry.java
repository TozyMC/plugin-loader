package com.github.tozymc.pluginloader.internal;

import com.github.tozymc.pluginloader.InjectionInstanceException;
import com.github.tozymc.pluginloader.annotation.Inject;
import com.github.tozymc.pluginloader.instance.InstanceProvider;
import com.github.tozymc.pluginloader.instance.InstanceProviderRegistry;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unchecked")
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
    return (InstanceProvider<T>) subMap.get(name);
  }

  void injectInstances(PluginContainerImpl container) throws InjectionInstanceException {
    var fields = container.info().loadedMainClass().getDeclaredFields();
    for (var field : fields) {
      if (!field.isAnnotationPresent(Inject.class)) {
        continue;
      }
      try {
        field.setAccessible(true);
      } catch (Exception e) {
        throw new InjectionInstanceException("Cannot access field: " + field.getName());
      }
      var name = field.getAnnotation(Inject.class).name();
      var provider = get(field.getType(), name);
      if (provider == null) {
        throw new InjectionInstanceException(
            "Instance of %s:%s is null".formatted(name, field.getType().getSimpleName()));
      }
      try {
        field.set(container.pluginInstance(), provider.get(container));
      } catch (IllegalAccessException e) {
        throw new InjectionInstanceException(
            "Cannot inject instance %s:%s".formatted(name, field.getType().getSimpleName()));
      }
    }
  }

  private static final class InstanceProviderMap<T> extends HashMap<String, InstanceProvider<T>> {
    private void put(String name, InstanceProvider<?> creator) {
      super.put(name, (InstanceProvider<T>) creator);
    }
  }
}
