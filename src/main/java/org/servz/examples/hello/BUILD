package(default_visibility = ["//visibility:public"])

java_binary(
  name = "main",
  main_class = "org.servz.examples.hello.Main",
  srcs = glob(['*.java']),
  deps = [
    "//src/main/java/org/servz/handler",
    "//src/main/java/org/servz/handler:annotations",
    "//src/main/java/org/servz/compiler",
    "//src/main/java/org/servz/core",
    "//src/main/java/org/servz/logging",
    "//third_party/logging:logging-api",
    "//third_party/logging:logging-impl-stdout",
    "//third_party/dagger",
  ],
)
