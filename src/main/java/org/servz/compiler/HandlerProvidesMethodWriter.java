package org.servz.compiler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Provider;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;

import org.servz.handler.Annotations.Handles;
import org.servz.handler.Handler;
import org.servz.handler.HandlerMetadata;

import com.google.common.collect.ImmutableSet;

import dagger.Provides;
import dagger.Provides.Type;

/**
 * A writer for the method which an original method annotated with {@link Handles} and generated a
 * method which knows how to provide Dagger with an actual object which implements {@link Handler}.
 */
public class HandlerProvidesMethodWriter {
  private final TypeName originalModuleName;
  private final String originalHandlesMethodName;
  private final String handlerClassName;
  private final List<? extends VariableElement> handleParameterTypes;
  private final TriggerMetadata triggerMetadata;

  private final Set<Class<?>> requiredImports;

  public HandlerProvidesMethodWriter(
      TriggerMetadata triggerMetadata,
      TypeName originalModuleName,
      String originalHandlesMethodName,
      String handlerClassName,
      List<? extends VariableElement> handleParameterTypes) {
    this.triggerMetadata = triggerMetadata;
    this.originalHandlesMethodName = originalHandlesMethodName;
    this.originalModuleName = originalModuleName;
    this.handlerClassName = handlerClassName;
    this.handleParameterTypes = handleParameterTypes;
    this.requiredImports = new HashSet<>();
  }

  public ImmutableSet<Class<?>> requiredImports() {
    requiredImports.add(HandlerMetadata.class);
    requiredImports.add(Provider.class);
    requiredImports.add(Provides.class);
    requiredImports.add(Method.class);
    requiredImports.add(MethodUtil.class);
    requiredImports.add(triggerMetadata.getHandlerBindingAnnotation());

    return ImmutableSet.copyOf(requiredImports);
  }

  /**
   * Returns a string which contains a single Dagger provider method. It returns a handler which
   * can be used to execute the code in the original {@link Handles} method. The provides method is
   * annotated with the appropriate binding annotation for the trigger.
   */
  public String write() {
    String providesMethodName = originalModuleName.simpleName() + "_"
        + originalHandlesMethodName + "_"
        + "provide" + triggerMetadata.getTypeInfix() + "_"
        + HandlerMetadata.class.getSimpleName();

    String handlerProviderParamName = "handlerProvider";
    String handlerMethodName = "handlerMethod";

    return new StringBuilder()
        .append(writeProvidesSetLine())
        .append("@" + triggerMetadata.getHandlerBindingAnnotation().getSimpleName() + "\n")
        .append(HandlerMetadata.class.getSimpleName() + " " + providesMethodName + "(")
        .append(writeProviderType(handlerClassName))
        .append(" " + handlerProviderParamName + ") ")
        .append("{\n")
        .append(writeFindMethodLine(handlerMethodName))
        .append(writeReturnHandlerMetadataLine(handlerMethodName, handlerProviderParamName))
        .append("}\n")
        .toString();
  }

  private StringBuilder writeReturnHandlerMetadataLine(
      String handlerMethodName, String handlerProviderParamName) {
    return new StringBuilder()
        .append("return new " + HandlerMetadata.class.getSimpleName())
        .append("(" + handlerMethodName + ", " + handlerProviderParamName + ");\n");
  }

  private StringBuilder writeFindMethodLine(String handlerMethodName) {
    ArrayList<String> findMethodArgs = new ArrayList<>();
    findMethodArgs.add(originalModuleName.canonicalName() + ".class");
    findMethodArgs.add("\"" + originalHandlesMethodName + "\"");
    for (VariableElement paramType : handleParameterTypes) {
      DeclaredType declaredType = (DeclaredType) paramType.asType();
      findMethodArgs.add(declaredType.asElement().toString() + ".class");
    }

    String findMethodArgsString = String.join(", ", findMethodArgs);
    return new StringBuilder()
        .append(Method.class.getSimpleName() + " " + handlerMethodName + " = ")
        .append(MethodUtil.class.getSimpleName() + ".getMethodOrThrow")
        .append("(" + findMethodArgsString + ");\n");
  }

  private StringBuilder writeProviderType(String providedTypeName) {
    return new StringBuilder()
        .append(Provider.class.getSimpleName())
        .append("<")
        .append(providedTypeName)
        .append(">");
  }

  /**
   * Example: @code{@Provides(type = Provides.Type.SET)}.
   */
  private StringBuilder writeProvidesSetLine() {
    Type setEnum = Provides.Type.SET;
    return new StringBuilder()
        .append("@" + Provides.class.getSimpleName())
        .append("(")
        .append("type = " + setEnum.getDeclaringClass().getCanonicalName() + "." + setEnum.name())
        .append(")")
        .append("\n");
  }
}
