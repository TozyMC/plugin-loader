package com.github.tozymc.pluginloader.internal;

import static java.nio.file.Files.newInputStream;
import static java.util.Objects.requireNonNullElse;
import static java.util.Optional.ofNullable;

import com.github.tozymc.pluginloader.PluginInfo;
import com.github.tozymc.pluginloader.annotation.Plugin;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

final class CandidatePluginInfo implements PluginInfo {
  static final String PLUGIN_INFO_FILENAME = "plugin-info.json";
  private static final Gson GSON = new Gson();

  private String id;
  private @Expose Path source;
  private String mainClass;
  private String name;
  private String version;
  private String description;
  private List<String> authors = List.of();
  private LinkedHashSet<CandidateDependency> dependencies = new LinkedHashSet<>();
  private CandidateLibraries libraries;

  private CandidatePluginInfo() {
  }

  static CandidatePluginInfo deserialize(Path source) throws IOException {
    try (JarInputStream in = new JarInputStream(new BufferedInputStream(newInputStream(source)))) {
      JarEntry entry;
      while ((entry = in.getNextJarEntry()) != null) {
        if (!entry.getName().equals(PLUGIN_INFO_FILENAME)) {
          continue;
        }
        try (var pluginInfoReader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
          return GSON.fromJson(pluginInfoReader, CandidatePluginInfo.class);
        }
      }
    }
    return null;
  }

  static String serializePluginAnnotation(Class<?> mainClass, Plugin plugin) {
    var info = new CandidatePluginInfo();
    info.id = plugin.id();
    info.mainClass = mainClass.getName();
    info.name = plugin.name().isBlank() ? null : plugin.name();
    info.version = plugin.version().isBlank() ? null : plugin.version();
    info.description = plugin.description().isBlank() ? null : plugin.description();
    info.authors = plugin.authors().length == 0 ? null : Arrays.asList(plugin.authors());
    info.dependencies =
        plugin.dependencies().length == 0 ? null : Arrays.stream(plugin.dependencies()).map(d -> {
          var newD = new CandidateDependency();
          newD.id = d.id();
          newD.optional = d.optional();
          return newD;
        }).collect(Collectors.toCollection(LinkedHashSet::new));
    info.libraries =
        plugin.libraries().repositories().length == 0 && plugin.libraries().artifacts().length == 0
            ? null : new CandidateLibraries();
    if (info.libraries != null) {
      info.libraries.repositories = new LinkedHashSet<>(List.of(plugin.libraries().repositories()));
      info.libraries.artifacts = new LinkedHashSet<>(List.of(plugin.libraries().artifacts()));
    }
    return GSON.toJson(info, CandidatePluginInfo.class);
  }

  @NotNull
  @Override
  public String id() {
    return id;
  }

  public void source(Path source) {
    this.source = source;
  }

  @NotNull
  @Override
  public Path source() {
    return source;
  }

  @NotNull
  @Override
  public String mainClass() {
    return mainClass;
  }

  @Override
  public @NotNull Optional<String> name() {
    return ofNullable(name);
  }

  @Override
  public @NotNull Optional<String> version() {
    return ofNullable(version);
  }

  @Override
  public @NotNull Optional<String> description() {
    return ofNullable(description);
  }

  @NotNull
  @Override
  public List<String> authors() {
    return authors;
  }

  @NotNull
  @Override
  public LinkedHashSet<CandidateDependency> dependencies() {
    return dependencies;
  }

  @NotNull
  @Override
  public CandidateLibraries libraries() {
    return requireNonNullElse(libraries, new CandidateLibraries());
  }

  @Override
  public String toString() {
    return "CandidatePluginInfo{id='%s', source=%s, mainClass='%s', name='%s', version='%s', description='%s', authors=%s, dependencies=%s, libraries=%s}".formatted(
        id, source, mainClass, name, version, description, authors, dependencies, libraries);
  }

  static class CandidateDependency implements Dependency {
    private String id;
    private boolean optional = false;

    private CandidateDependency() {
    }

    @NotNull
    @Override
    public String id() {
      return id;
    }

    @Override
    public boolean optional() {
      return optional;
    }

    @Override
    public String toString() {
      return "CandidateDependency{id='%s', optional=%s}".formatted(id, optional);
    }
  }

  static class CandidateLibraries implements Libraries {
    private LinkedHashSet<String> repositories = new LinkedHashSet<>();
    private LinkedHashSet<String> artifacts = new LinkedHashSet<>();

    private CandidateLibraries() {
    }

    @NotNull
    @Override
    public LinkedHashSet<String> repositories() {
      return repositories;
    }

    @NotNull
    @Override
    public LinkedHashSet<String> artifacts() {
      return artifacts;
    }

    @Override
    public String toString() {
      return "CandidateLibraries{repositories=%s, artifacts=%s}".formatted(repositories, artifacts);
    }
  }
}
