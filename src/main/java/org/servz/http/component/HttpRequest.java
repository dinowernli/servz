package org.servz.http.component;

import java.net.URI;

import io.netty.handler.codec.http.HttpMethod;

/**
 * The main interface for incoming http requests.
 */
public interface HttpRequest {
  /**
   * Returns the requested URI for this request.
   */
  URI uri();

  /**
   * The method for this request.
   */
  HttpMethod method();
}
