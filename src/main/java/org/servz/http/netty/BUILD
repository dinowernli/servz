package(default_visibility = ["//visibility:public"])

java_library(
  name = "netty",
  srcs = glob(['*.java'], exclude = ["Annotations.java"]),
  deps = [
    "//src/main/java/org/servz/core",
    "//src/main/java/org/servz/handler",
    "//src/main/java/org/servz/http:annotations",
    "//src/main/java/org/servz/http/component",
    "//src/main/java/org/servz/logging",
    "//third_party/dagger",
    "//third_party/logging:logging-api",
    "//third_party/netty",
    "//third_party/guava",
  ]
)
