package com.github.tozymc.pluginloader.internal;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import com.github.tozymc.pluginloader.PluginInfo;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

class LoadedPluginInfo implements PluginInfo {
  private final String id;
  private final Path source;
  private final String mainClass;
  private final String name;
  private final String version;
  private final String description;
  private final List<String> authors;
  private final Set<Dependency> dependencies;
  private final ImmutableLibraries libraries;

  private Class<?> loadedMainClass;

  LoadedPluginInfo(CandidatePluginInfo candidate) {
    this.id = requireNonNull(candidate.id());
    this.source = requireNonNull(candidate.source());
    this.mainClass = requireNonNull(candidate.mainClass());
    this.name = candidate.name().orElse(null);
    this.version = candidate.version().orElse(null);
    this.description = candidate.description().orElse(null);
    this.authors = List.copyOf(candidate.authors());
    this.dependencies = Set.copyOf(candidate.dependencies().stream()
        .map(d -> new ImmutableDependency(d.id(), d.optional()))
        .collect(Collectors.toCollection(LinkedHashSet::new)));
    this.libraries = new ImmutableLibraries(candidate.libraries().repositories(),
        candidate.libraries().artifacts());
  }

  @NotNull
  @Override
  public String id() {
    return id;
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
  public Set<Dependency> dependencies() {
    return dependencies;
  }

  @NotNull
  @Override
  public LoadedPluginInfo.ImmutableLibraries libraries() {
    return libraries;
  }

  public Class<?> loadedMainClass() {
    return loadedMainClass;
  }

  public void loadMainClass(Class<?> clazz) {
    this.loadedMainClass = clazz;
  }

  record ImmutableDependency(String id, boolean optional) implements Dependency {
  }

  record ImmutableLibraries(Set<String> repositories, Set<String> artifacts) implements Libraries {
  }
}
