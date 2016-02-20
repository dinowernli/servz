package(default_visibility = ["//visibility:public"])

java_library(
  name = "compiler",
  exported_plugins = [
    ":compiler_internal",
  ],
  # These exports are necessary because the generated code depends on these
  # targets. As such, users of this library need to depend directly on the
  # contained targets.
  exports = [
    ":method-util",
  ],
)

java_plugin(
  visibility = ["//visibility:private"],
  name = "compiler_internal",
  srcs = glob(['*.java']),
  processor_class = "org.servz.compiler.HandlerModuleAnnotationProcessor",
  deps = [
    "//src/main/java/org/servz/handler:annotations",
    "//src/main/java/org/servz/handler",
    "//src/main/java/org/servz/core",
    "//src/main/java/org/servz/http:annotations",
    "//third_party/dagger",
    "//third_party/guava",
  ],
)

java_library(
  name = "method-util",
  visibility = ["//visibility:private"],
  srcs = [
    "MethodUtil.java",
  ],
)
