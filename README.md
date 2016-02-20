## Servz ##

Servz is a Java library which facilitates writing long-lived binaries, such as servers or daemons, in a modular way.

Each bit of logic is implemented as a Servz module and the final binary is created by assembling the modules into a Servz server. In addition to providing the skeleton for running the binaries, the Servz library also comes with a set of modules used for common logic, such as starting an http server.

## Requirements ##

Servz is built using Bazel. As such, the only dependencies required to build are:

* Bazel
* Java runtime

Bazel takes care of pulling down the Java libraries which Servz depends on, such as:

* Dagger
* Guava
* Netty
* Testing libraries such as JUnit, Mockito, Truth.

## Getting started ##

One of the example servers can be run by executing:

```
> bazel run src/main/java/org/servz/examples/requestscope:main
```

All tests can be run by executing:

```
> bazel test src/...
```

## License

Unless explicitly stated otherwise all files in this repository are licensed under the Apache Software License 2.0.

```
Copyright 2016 Dino Wernli

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
