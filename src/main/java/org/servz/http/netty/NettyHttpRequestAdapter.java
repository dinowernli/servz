package org.servz.http.netty;

import java.net.URI;
import java.net.URISyntaxException;

import org.servz.http.component.HttpRequest;

import io.netty.handler.codec.http.HttpMethod;

/**
 * An adapter class used to expose the interface provided by
 * {@link io.netty.handler.codec.http.HttpRequest} as a {@link HttpRequest}.
 */
class NettyHttpRequestAdapter implements HttpRequest {
  private final io.netty.handler.codec.http.HttpRequest nettyRequest;

  NettyHttpRequestAdapter(io.netty.handler.codec.http.HttpRequest nettyRequest) {
    this.nettyRequest = nettyRequest;
  }

  @Override
  public URI uri() {
    try {
      return new URI(nettyRequest.getUri());
    } catch (URISyntaxException e) {
      // TODO(dinowernli): Figure out how to propagate exceptions properly.
      throw new RuntimeException(e);
    }
  }

  @Override
  public HttpMethod method() {
    return nettyRequest.getMethod();
  }
}
