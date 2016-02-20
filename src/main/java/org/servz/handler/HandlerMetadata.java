package org.servz.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

import javax.inject.Provider;

import org.servz.handler.Annotations.Handles;

import com.google.common.base.Preconditions;

/**
 * Provides various bits of information about a handler method, such as a unique name and
 * information about the original handler method.
 */
public class HandlerMetadata {
  private final Method originalMethod;
  private final Provider<? extends Handler> handlerProvider;

  public HandlerMetadata(Method originalMethod, Provider<? extends Handler> handlerProvider) {
    Preconditions.checkArgument(originalMethod.isAnnotationPresent(Handles.class));
    this.handlerProvider = handlerProvider;
    this.originalMethod = originalMethod;
  }

  /** Returns whether the handler represented by this object has the supplied annotation. */
  public boolean hasAnnotation(Class<? extends Annotation> annotationClass) {
    return originalMethod.isAnnotationPresent(annotationClass);
  }

  /**
   * Returns the annotation instance of the supplied type present on the handler represented by
   * this object. Returns absent if the handler has not annotation of the supplied type.
   */
  public <A extends Annotation> Optional<A> getAnnotation(Class<A> annotationClass) {
    return Optional.ofNullable(originalMethod.getAnnotation(annotationClass));
  }

  /** Creates a new handler instance. */
  public Handler createHandler() {
    return handlerProvider.get();
  }

  /** Returns a name for the handler. The returned name is a valid java identifier. */
  public String name() {
    return originalMethod.getDeclaringClass().getSimpleName() + "." + originalMethod.getName();
  }
}