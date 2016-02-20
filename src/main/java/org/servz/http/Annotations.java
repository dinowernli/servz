package org.servz.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import org.servz.handler.Annotations.Handles;
import org.servz.handler.Annotations.Trigger;

/**
 * Annotations used in this package and exposed as part of the API of this package.
 */
public class Annotations {
  /**
   * An annotation for {@link Handles} methods indicating that they should run on http triggers
   * with the "get" method.
   */
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.METHOD })
  public @interface Get { String path(); };

  // TODO(dino): Consider making Get, Post, etc triggers or adding the path binding directly to the
  // HttpRequest trigger below.

  /** Binds the executor used to start the http server. */
  @Qualifier @interface HttpExecutor {}

  /** Binds the instance of the netty http server. */
  @Qualifier public @interface NettyServer {}

  /** A trigger for http requests. */
  @Trigger @Qualifier public @interface HttpRequest {}

  /** Binds a map of http parameters. */
  @Qualifier public @interface HttpParams {}
}
