package com.github.tozymc.pluginloader.internal;

import com.github.tozymc.pluginloader.InjectionInstanceException;
import com.github.tozymc.pluginloader.InvalidPluginException;
import com.github.tozymc.pluginloader.PluginContainer;
import com.github.tozymc.pluginloader.PluginInfo;
import com.github.tozymc.pluginloader.PluginInitializationException;
import com.github.tozymc.pluginloader.PluginProvider;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import org.jetbrains.annotations.NotNull;

final class JavaPluginProvider implements PluginProvider {
  private final JavaInstanceProviderRegistry instanceProviderRegistry;

  JavaPluginProvider(JavaInstanceProviderRegistry instanceProviderRegistry) {
    this.instanceProviderRegistry = instanceProviderRegistry;
  }

  @Override
  public @NotNull PluginInfo loadCandidate(@NotNull Path source) throws InvalidPluginException {
    CandidatePluginInfo candidate;

    try {
      candidate = CandidatePluginInfo.deserialize(source);
    } catch (IOException e) {
      throw new InvalidPluginException(
          "Reading %s error".formatted(CandidatePluginInfo.PLUGIN_INFO_FILENAME), e);
    }
    if (candidate == null) {
      throw new InvalidPluginException(CandidatePluginInfo.PLUGIN_INFO_FILENAME + " not found");
    }

    candidate.source(source);
    if (PluginInfo.ID_PATTERN.matcher(candidate.id()).matches()) {
      return candidate;
    }

    throw new InvalidPluginException(
        "Plugin ID %s is invalid. Must match: %s".formatted(candidate.id(),
            PluginInfo.ID_PATTERN.pattern()));
  }

  @Override
  public @NotNull PluginInfo createFromCandidate(@NotNull PluginInfo candidate,
      List<URL> libraryUrls) throws ClassNotFoundException {
    if (!(candidate instanceof CandidatePluginInfo)) {
      throw new IllegalArgumentException("Plugin info is not loaded by the plugin system");
    }

    var loadUrls = new URL[libraryUrls == null ? 1 : libraryUrls.size() + 1];
    try {
      loadUrls[0] = candidate.source().toUri().toURL();
    } catch (MalformedURLException ignored) {
    }
    if (libraryUrls != null) {
      var itr = libraryUrls.listIterator();
      while (itr.hasNext()) {
        var next = itr.next();
        loadUrls[itr.nextIndex()] = next;
      }
    }
    //noinspection resource
    var loader = new PluginClassLoader(loadUrls);
    loader.addToClassloaders();

    var loadedInfo = new LoadedPluginInfo((CandidatePluginInfo) candidate);
    loadedInfo.loadMainClass(loader.loadClass(candidate.mainClass()));
    return loadedInfo;
  }

  @Override
  public void create(@NotNull PluginContainer container)
      throws PluginInitializationException, InjectionInstanceException {
    if (!(container instanceof PluginContainerImpl containerInst)) {
      throw new IllegalArgumentException("Plugin info is not loaded by the plugin system");
    }

    Object instance;
    try {
      var constructor = containerInst.info().loadedMainClass().getDeclaredConstructor();
      constructor.setAccessible(true);
      instance = constructor.newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
             NoSuchMethodException e) {
      throw new PluginInitializationException("Cannot create plugin instance", e);
    }
    containerInst.plugin(instance);
    instanceProviderRegistry.injectInstances(containerInst);
  }
}
