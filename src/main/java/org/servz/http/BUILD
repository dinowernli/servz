package(default_visibility = ["//visibility:public"])

java_library(
  name = "http",
  srcs = glob(['*.java'], exclude = ["Annotations.java"]),
  deps = [
    "//src/main/java/org/servz/http:annotations",
    "//src/main/java/org/servz/http/netty",
    "//src/main/java/org/servz/handler",
    "//src/main/java/org/servz/handler:annotations",
    "//src/main/java/org/servz/compiler",
    "//src/main/java/org/servz/logging",
    "//third_party/dagger",
    "//third_party/logging:logging-api",
    "//third_party/guava",
  ]
)

java_library(
  name = "annotations",
  srcs = ["Annotations.java"],
  deps = [
    "//third_party/dagger",
    "//src/main/java/org/servz/handler:annotations",
  ]
)
