package(default_visibility = ["//visibility:public"])

java_library(
  name = "auto",
  exports = [
    "@auto-value-artifact//jar",
  ],
  exported_plugins = [
    ":auto-compiler",
  ],
  licenses = ["permissive"],
)

java_plugin(
  name = "auto-compiler",
  deps = [
    "@auto-value-artifact//jar",
  ],
  processor_class = "com.google.auto.value.processor.AutoValueProcessor",
  licenses = ["permissive"],
)