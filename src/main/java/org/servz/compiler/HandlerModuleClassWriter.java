package org.servz.compiler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.servz.handler.Annotations.Handles;
import org.servz.handler.Handler;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import dagger.Module;
import dagger.Provides;

class HandlerModuleClassWriter {
  private final WriterUtil writerUtil;

  private Optional<String> packageName;
  private Optional<String> className;
  private final Set<Class<?>> imports;
  private final List<HandlesBlockWriter> handlesBlockWriters;

  public HandlerModuleClassWriter(WriterUtil writerUtil) {
    this.writerUtil = writerUtil;
    this.packageName = Optional.empty();
    this.className = Optional.empty();
    this.imports = new HashSet<>();
    this.handlesBlockWriters = new ArrayList<>();
  }

  public String write() {
    validate();
    StringBuilder resultBuilder = new StringBuilder();

    // Package.
    resultBuilder.append(writerUtil.writePackage(packageName.get()));
    resultBuilder.append("\n");

    // Imports.
    ImmutableSet.Builder<Class<?>> importsToWriteBuilder = ImmutableSet.<Class<?>>builder()
        .addAll(imports);
    for (HandlesBlockWriter handlesBlock : handlesBlockWriters) {
      importsToWriteBuilder.addAll(handlesBlock.requiredImports());
    }
    ImmutableSet<Class<?>> importsToWrite = importsToWriteBuilder.build();
    resultBuilder.append(writerUtil.writeImports(importsToWrite));
    resultBuilder.append("\n");

    // Class body.
    resultBuilder.append(writerUtil.writeLine("@" + Module.class.getCanonicalName()));
    resultBuilder.append(writerUtil.writeLine("public class " + className.get() + " {"));
    for (HandlesBlockWriter handlesBlock : handlesBlockWriters) {
      resultBuilder.append(handlesBlock.write());
    }
    resultBuilder.append(writerUtil.writeLine("}"));

    return resultBuilder.toString();
  }

  public HandlerModuleClassWriter setPackageName(String packageName) {
    this.packageName = Optional.of(packageName);
    return this;
  }

  public HandlerModuleClassWriter setClassName(String className) {
    this.className = Optional.of(className);
    return this;
  }

  public HandlerModuleClassWriter addImport(Class<?> importClass) {
    imports.add(importClass);
    return this;
  }

  public HandlerModuleClassWriter addHandlesBlock(
      HandlerClassWriter handlerClassWriter,
      HandlerProvidesMethodWriter providesMethodWriter) {
    handlesBlockWriters.add(new HandlesBlockWriter(handlerClassWriter, providesMethodWriter));
    return this;
  }

  private void validate() {
    Preconditions.checkState(packageName.isPresent());
    Preconditions.checkState(className.isPresent());

    // TODO(dino): Add more.
  }

  /**
   * A class which knows how to write the entire block of code associated with a {@link Handles}
   * method. This includes a class which implements {@link Handler} and a method annotated with
   * {@link Provides}.
   */
  private class HandlesBlockWriter {
    private final HandlerClassWriter handlerClassWriter;
    private final HandlerProvidesMethodWriter providesMethodWriter;

    private HandlesBlockWriter(
        HandlerClassWriter classWriter, HandlerProvidesMethodWriter methodWriter) {
      this.handlerClassWriter = classWriter;
      this.providesMethodWriter = methodWriter;
    }

    String write() {
      return new StringBuilder()
          .append(handlerClassWriter.write())
          .append("\n")
          .append(providesMethodWriter.write())
          .append("\n")
          .toString();
    }

    ImmutableSet<Class<?>> requiredImports() {
      return providesMethodWriter.requiredImports();
    }
  }
}
