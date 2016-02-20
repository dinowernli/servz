package org.servz.handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

public class Annotations {
  /**
   * Annotates a method which handles a trigger.
   */
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Handles {};

  /**
   * A class which contains handler methods, i.e., method annotates with {@link Handles}.
   */
  public @interface HandlerModule {};
  // TODO(dino): Document that these must have a no-arg constructor or generate the code that
  // just passes a dummy instance rather than actually constructing anything.

  /**
   * A trigger is a situation in which a handler gets executed. The handler is said to handle the
   * trigger.
   */
  @Target(ElementType.ANNOTATION_TYPE)
  public @interface Trigger {};

  /**
   * A trigger annotation for handler methods which are run on server startup. Handler methods
   * annotated with this annotation should not block.
   */
  @Trigger @Qualifier public @interface Startup {};

  /**
   * A trigger annotation for handler methods which are run on server shutdown. Handler methods
   * annotated with this annotation should not block.
   */
  @Trigger @Qualifier public @interface Shutdown {};
}
