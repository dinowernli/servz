package org.servz.compiler;

import java.lang.annotation.Annotation;

import javax.inject.Qualifier;

import org.servz.handler.Annotations.Handles;
import org.servz.handler.Annotations.Trigger;

import com.google.common.base.Preconditions;

/**
 * Holds metadata and provides information about a {@link Trigger} for use during code generation.
 */
public class TriggerMetadata {
  private final Class<? extends Annotation> triggerAnnotation;

  /**
   * @param triggerAnnotation an annotation type which must be annotated with {@link Trigger}.
   */
  public TriggerMetadata(Class<? extends Annotation> triggerAnnotation) {
    Preconditions.checkArgument(triggerAnnotation.isAnnotationPresent(Qualifier.class),
        "The trigger annotation must also be a Dagger qualifier in order to inject the handlers.");
    this.triggerAnnotation = triggerAnnotation;
  }

  /**
   * Returns an annotation used to bind this trigger's set of handlers for dagger provisioning.
   */
  public Class<? extends Annotation> getHandlerBindingAnnotation() {
    return triggerAnnotation;
  }

  /**
   * Returns an annotation which needs to be present on a handler method (in addition to
   * {@link Handles}.
   */
  public Class<? extends Annotation> getHandlesMethodAnnotation() {
    return triggerAnnotation;
  }

  /**
   * Returns a string which can be added to generated types to indicate that they are generated for
   * this {@link Trigger}.
   */
  public String getTypeInfix() {
    return triggerAnnotation.getSimpleName();
  }
}
