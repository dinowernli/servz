package(default_visibility = ["//visibility:public"])

java_library(
  name = "dagger",
  exported_plugins = [
    ":dagger-compiler",
  ],
  exports = [
    "@dagger_artifact//jar",
    "@dagger_producers_artifact//jar",
    "@javax_inject_artifact//jar",
  ],
  licenses = ["permissive"],
)

java_plugin(
  name = "dagger-compiler",
  deps = [
    "@dagger_compiler_artifact//jar",
    "@dagger_producers_artifact//jar",
    "//third_party/guava",
    "@javax_inject_artifact//jar",
    "@dagger_artifact//jar",
  ],
  processor_class = "dagger.internal.codegen.ComponentProcessor",
  licenses = ["permissive"],
)
