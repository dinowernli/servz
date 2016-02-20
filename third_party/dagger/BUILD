package(default_visibility = ["//visibility:public"])

java_library(
  name = "dagger",
  exported_plugins = [
    ":dagger-compiler",
  ],
  exports = [
    "@dagger-artifact//jar",
    "@dagger-producers-artifact//jar",
    "@javax-inject-artifact//jar",
  ],
  licenses = ["permissive"],
)

java_plugin(
  name = "dagger-compiler",
  deps = [
    "@dagger-compiler-artifact//jar",
    "@dagger-producers-artifact//jar",
    "//third_party/guava",
    "@javax-inject-artifact//jar",
    "@dagger-artifact//jar",
  ],
  processor_class = "dagger.internal.codegen.ComponentProcessor",
  licenses = ["permissive"],
)