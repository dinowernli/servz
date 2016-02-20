package org.servz.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.VariableElement;

import org.servz.handler.Handler;

import com.google.common.base.Preconditions;

class HandlerClassWriter {
  private static final Class<?> HANDLER_SUPERTYPE = Handler.class;

  private final WriterUtil writerUtil;
  private final TypeName originalModuleName;
  private final String originalHandlesMethodName;

  /**
   * Whether or not the original method returned "void". This is used to decide which value to have
   * the {@link Handler} implementation return.
   */
  private final boolean isOriginalHandlesMethodReturnTypeVoid;

  private Optional<String> className;
  private final List<VariableElement> handleParameterTypes;

  HandlerClassWriter(
      WriterUtil writerUtil,
      TypeName originalModuleName,
      String originalHandlesMethodName,
      boolean isOriginalHandlesMethodReturnTypeVoid) {
    this.writerUtil = writerUtil;
    this.originalModuleName = originalModuleName;
    this.originalHandlesMethodName = originalHandlesMethodName;
    this.isOriginalHandlesMethodReturnTypeVoid = isOriginalHandlesMethodReturnTypeVoid;

    this.className = Optional.empty();
    this.handleParameterTypes = new ArrayList<>();
  }

  private String canonicalType(VariableElement param) {
    // TODO(dino): There has to be a better way to do this...
    return param.asType().toString();
  }

  public String write() {
    validate();
    StringBuilder resultBuilder = new StringBuilder();

    // Class body.
    resultBuilder.append(writerUtil.writeLine("static class " + className.get()
        + " implements " + HANDLER_SUPERTYPE.getCanonicalName() + " {"));

    ArrayList<String> fieldNames = new ArrayList<>();
    for (VariableElement param : handleParameterTypes) {
      String fieldName = "field" + fieldNames.size();
      String fieldType = canonicalType(param);
      resultBuilder.append(writerUtil.writeFinalField(fieldType, fieldName));
      fieldNames.add(fieldName);
    }

    resultBuilder.append(writeConstructor(fieldNames));
    resultBuilder.append(writeHandleMethod(fieldNames));
    resultBuilder.append(writerUtil.writeLine("}"));  // Class body.

    return resultBuilder.toString();
  }

  public HandlerClassWriter setClassName(String className) {
    this.className = Optional.of(className);
    return this;
  }

  public HandlerClassWriter addHandleParameterTypes(
      List<? extends VariableElement> parameterTypes) {
    handleParameterTypes.addAll(parameterTypes);
    return this;
  }

  /**
   * Write the implementation of {@link Handler#handle()} which calls the handle method provided by
   * the original module. It uses the supplied variable names are arguments (in order).
   */
  private String writeHandleMethod(ArrayList<String> argNames) {
    String receiver = "new " + originalModuleName.simpleName() + "()";
    String[] argNamesArray = argNames.toArray(new String[0]);
    String call = writerUtil.writeMethodCall(receiver, originalHandlesMethodName, argNamesArray);

    // TODO(dino): Handle different return types for non-startup handlers.

    StringBuilder resultBuilder = new StringBuilder()
        .append(writerUtil.writeLine("public Object handle() {"));

    if (isOriginalHandlesMethodReturnTypeVoid) {
      resultBuilder
          .append(call)
          .append(writerUtil.writeLine("return null;"));
    } else {
      resultBuilder.append(writerUtil.writeLine("return " + call));
    }

    return writerUtil.writeLine(resultBuilder
        .append(writerUtil.writeLine("}"))
        .toString());
  }

  private StringBuilder writeConstructorArg(VariableElement paramType, String argName) {
    StringBuilder constructorArg = new StringBuilder();
    for (AnnotationMirror annotationType : paramType.getAnnotationMirrors()) {
      constructorArg.append(annotationType.toString() + " ");
    }
    constructorArg.append(paramType.asType().toString() + " " + argName);
    return constructorArg;
  }

  private String writeConstructor(ArrayList<String> fieldNames) {
    // Write the constructor signature, including all arguments and their annotations.
    ArrayList<String> constructorArgNames = new ArrayList<>();
    StringBuilder constructorArgs = new StringBuilder();
    boolean first = true;
    for (VariableElement paramType : handleParameterTypes) {
      if (!first) {
        constructorArgs.append(", ");
      }
      first = false;

      String argName = "arg" + constructorArgNames.size();
      constructorArgs.append(writeConstructorArg(paramType, argName));
      constructorArgNames.add(argName);
    }

    // Write the constructor body which assigns the arguments to the respective fields.
    Preconditions.checkState(constructorArgNames.size() == fieldNames.size());
    StringBuilder constructorBody = new StringBuilder();
    for (int i = 0; i < constructorArgNames.size(); ++i) {
      constructorBody.append(writerUtil.writeLine(
          fieldNames.get(i) + " = " + constructorArgNames.get(i) + ";"));
    }

    // Put the pieces together.
    return new StringBuilder()
        .append(writerUtil.writeLine("@" + Inject.class.getCanonicalName()))
        .append(writerUtil.writeLine(className.get() + "(" + constructorArgs + ") {"))
        .append(constructorBody)
        .append(writerUtil.writeLine("}"))
        .toString();
  }

  private void validate() {
    Preconditions.checkState(className.isPresent());
  }
}
