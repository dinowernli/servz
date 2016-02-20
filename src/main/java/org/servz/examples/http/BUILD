package(default_visibility = ["//visibility:public"])

HANDLER_MODULE_SRCS = [
  "HelloHttpHandlerModule.java",
]

java_binary(
  name = "main",
  main_class = "org.servz.examples.http.Main",
  srcs = glob(['*.java'], exclude = HANDLER_MODULE_SRCS),
  deps = [
    ":handler-module",
    "//src/main/java/org/servz/handler:annotations",
    "//src/main/java/org/servz/compiler",
    "//src/main/java/org/servz/core",
    "//src/main/java/org/servz/handler",
    "//src/main/java/org/servz/http",
    "//src/main/java/org/servz/logging",
    "//src/main/java/org/servz/http/component",
    "//third_party/logging:logging-impl-stdout",
    "//third_party/dagger",
    "//third_party/logging:logging-api",
  ],
)

java_library(
  name = "handler-module",
  srcs = HANDLER_MODULE_SRCS,
  deps = [
    "//src/main/java/org/servz/compiler",
    "//src/main/java/org/servz/http:annotations",
    "//src/main/java/org/servz/handler:annotations",
    "//src/main/java/org/servz/handler",
    "//src/main/java/org/servz/http/component",
    "//src/main/java/org/servz/core",
    "//src/main/java/org/servz/logging",
    "//third_party/netty",
    "//third_party/guava",
    "//third_party/dagger",
    "//third_party/logging:logging-api",
  ],
)
