package com.github.tozymc.pluginloader.internal;

import com.github.tozymc.pluginloader.PluginSystem;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class PluginClassLoader extends URLClassLoader {
  private static final Set<PluginClassLoader> loaders = new CopyOnWriteArraySet<>();

  static {
    ClassLoader.registerAsParallelCapable();
  }

  PluginClassLoader(URL[] urls) {
    super(urls, PluginSystem.class.getClassLoader());
  }

  public void addToClassloaders() {
    loaders.add(this);
  }

  void addToClasspath(URL url) {
    addURL(url);
  }

  @Override
  public void close() throws IOException {
    loaders.remove(this);
    super.close();
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    return loadClass0(name, resolve, true);
  }

  private Class<?> loadClass0(String name, boolean resolve, boolean checkOther)
      throws ClassNotFoundException {
    try {
      return super.loadClass(name, resolve);
    } catch (ClassNotFoundException ignored) {
    }

    if (checkOther) {
      for (var loader : loaders) {
        if (loader == this) {
          continue;
        }
        try {
          return loader.loadClass0(name, resolve, false);
        } catch (ClassNotFoundException ignored) {
        }
      }
    }

    throw new ClassNotFoundException(name);
  }
}
