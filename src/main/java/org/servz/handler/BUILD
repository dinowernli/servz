package(default_visibility = ["//visibility:public"])

java_library(
  name = "handler",
  srcs = glob(['*.java'], exclude = ["Annotations.java"]),
  deps = [
    ":annotations",
    "//third_party/dagger",
    "//third_party/guava",
  ]
)

java_library(
  name = "annotations",
  srcs = ["Annotations.java"],
  deps = [
    "//third_party/dagger",
  ]
)