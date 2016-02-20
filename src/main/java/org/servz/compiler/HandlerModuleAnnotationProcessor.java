package org.servz.compiler;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import org.servz.handler.Annotations.HandlerModule;
import org.servz.handler.Annotations.Handles;
import org.servz.handler.Annotations.Shutdown;
import org.servz.handler.Annotations.Startup;
import org.servz.http.Annotations.HttpRequest;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * An annotations processor for classes annotated with {@link HandlerModule}.
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class HandlerModuleAnnotationProcessor extends AbstractProcessor {
  private static final String PREFIX = "Servz";
  private static final ImmutableSet<TriggerMetadata> KNOWN_TRIGGERS = ImmutableSet.of(
      new TriggerMetadata(Startup.class),
      new TriggerMetadata(Shutdown.class),
      new TriggerMetadata(HttpRequest.class));

  // TODO(dino): Validation ideas:
  // - Check that there is only one handles of the same type per handler method (multiple Gets).

  @Override
  public final ImmutableSet<String> getSupportedAnnotationTypes() {
    return ImmutableSet.of(HandlerModule.class.getCanonicalName());
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    for (Element element : roundEnv.getElementsAnnotatedWith(HandlerModule.class)) {
      try {
        processHandlerModule((TypeElement) element);
      } catch (ValidationException e) {
        processingEnv.getMessager().printMessage(
            Kind.WARNING, "Skipped element due to exception: " + e.toString(), element);
      }
    }

    // Leave the module unclaimed so that the dagger compiler can do its thing.
    return false;
  }

  private void validateHandlerModuleClass(TypeElement handlerModuleClass)
      throws ValidationException {
    if (handlerModuleClass.getEnclosingElement().getKind() != ElementKind.PACKAGE) {
      throw new ValidationException("Type element " + handlerModuleClass.getQualifiedName()
          + " must not be a nested class.");
    }
  }

  private void processHandlerModule(TypeElement handlerModuleClass) throws ValidationException {
    validateHandlerModuleClass(handlerModuleClass);
    WriterUtil writerUtil = new WriterUtil();

    // Consruct the name and package for the generated class.
    TypeName originalHandlerModuleName = TypeName.fromTypeElement(handlerModuleClass);
    String simpleName = String.format("%s_%s", PREFIX, originalHandlerModuleName.simpleName());
    String fullName = String.format("%s.%s", originalHandlerModuleName.packageName(), simpleName);

    HandlerModuleClassWriter moduleClassWriter = new HandlerModuleClassWriter(writerUtil)
        .setClassName(simpleName)
        .setPackageName(originalHandlerModuleName.packageName());

    // Handle the code generation for all the triggers.
    for (TriggerMetadata triggerMeta : KNOWN_TRIGGERS) {
      for (ExecutableElement handlesMethod : getHandlerMethods(triggerMeta, handlerModuleClass)) {
        HandlesMethodParser parser = new HandlesMethodParser(
            writerUtil, originalHandlerModuleName, handlesMethod, triggerMeta);
        moduleClassWriter.addHandlesBlock(
            parser.handlerClassWriter(), parser.handlerProvidesMethodWriter());
      }
    }

    // Actually write the generated file.
    try {
      JavaFileObject moduleFile = processingEnv.getFiler().createSourceFile(fullName);
      try (Writer writer = moduleFile.openWriter()) {
        writer.write(moduleClassWriter.write());
      }
    } catch (IOException e) {
      processingEnv.getMessager().printMessage(Kind.ERROR, e.toString(), handlerModuleClass);
    }
  }

  /**
   * Returns all the methods which are annotated such that they can handle the supplied trigger.
   */
  private ImmutableList<ExecutableElement> getHandlerMethods(
      TriggerMetadata triggerMetadata, TypeElement handlerModuleClass) {
    ImmutableList.Builder<ExecutableElement> resultBuilder = ImmutableList.builder();
    for (Element child : handlerModuleClass.getEnclosedElements()) {
      if (child.getKind() != ElementKind.METHOD) {
        continue;
      }
      if (child.getAnnotation(Handles.class) == null) {
        continue;
      }
      if (child.getAnnotation(triggerMetadata.getHandlesMethodAnnotation()) == null) {
        continue;
      }
      resultBuilder.add((ExecutableElement) child);
    }
    return resultBuilder.build();
  }

  /**
   * Extracts relevant information from the {@link Handles}-annotated method and passes it into the
   * writers needed to generate code.
   */
  private static class HandlesMethodParser {
    private final WriterUtil writerUtil;
    private final ExecutableElement handlesMethod;
    private final TypeName originalModuleName;
    private final TriggerMetadata triggerMetadata;

    public HandlesMethodParser(
        WriterUtil writerUtil,
        TypeName originalModuleName,
        ExecutableElement startupMethod,
        TriggerMetadata triggerMetadata) {
      this.writerUtil = writerUtil;
      this.originalModuleName = originalModuleName;
      this.handlesMethod = startupMethod;
      this.triggerMetadata = triggerMetadata;
    }

    HandlerProvidesMethodWriter handlerProvidesMethodWriter() {
      return new HandlerProvidesMethodWriter(
          triggerMetadata,
          originalModuleName,
          originalHandlerMethodName(),
          handlerClassName(),
          handlesMethod.getParameters());
    }

    HandlerClassWriter handlerClassWriter() {
      boolean returnsVoid = handlesMethod.getReturnType().toString().equals("void");
      return new HandlerClassWriter(
          writerUtil, originalModuleName, originalHandlerMethodName(), returnsVoid)
              .setClassName(handlerClassName())
              .addHandleParameterTypes(handlesMethod.getParameters());
    }

    private String originalHandlerMethodName() {
      return handlesMethod.getSimpleName().toString();
    }

    private String handlerClassName() {
      String originalHandlesMethodName = handlesMethod.getSimpleName().toString();
      return PREFIX
          + "_" + originalHandlesMethodName
          + "_" + originalModuleName.simpleName()
          + "_" + triggerMetadata.getTypeInfix()
          + "_" + "Handler";
    }
  }
}
