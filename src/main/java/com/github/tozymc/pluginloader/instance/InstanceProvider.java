package com.github.tozymc.pluginloader.instance;

import com.github.tozymc.pluginloader.PluginContainer;
import java.util.function.Function;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface InstanceProvider<T> {
  @Contract(value = "_ -> new", pure = true)
  static <T> @NotNull InstanceProvider<T> ofStatic(@NotNull T instance) {
    return new StaticInstanceProvider<>(instance);
  }

  @Contract(pure = true)
  static <T> @NotNull InstanceProvider<T> adapt(
      @NotNull Function<PluginContainer, ? extends T> mappingFn) {
    return mappingFn::apply;
  }

  static <T, P> @NotNull InstanceProvider<T> fromPluginFunction(
      @NotNull Function<@Nullable P, ? extends T> mapping) {
    //noinspection unchecked
    return container -> mapping.apply((P) container.plugin().orElse(null));
  }

  @NotNull T get(@NotNull PluginContainer container);
}
