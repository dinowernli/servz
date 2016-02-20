package org.servz.http.netty;

import java.util.Optional;

import javax.inject.Inject;

import org.servz.core.ServerComponent;
import org.servz.handler.Handler;
import org.servz.handler.HandlerMetadata;
import org.servz.http.component.HttpHandlerMap;
import org.servz.http.component.HttpRequest;
import org.servz.http.component.HttpRequestComponent;
import org.servz.http.component.HttpRequestModule;
import org.servz.http.component.HttpResponse;
import org.servz.http.component.WithRequestScope;
import org.servz.logging.Logging;
import org.slf4j.Logger;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * Knows how to process http requests by finding an appropriate {@link Handler} and running it.
 */
class HttpRequestProcessor {
  static final Logger logger = Logging.getLoggerForClass();

  private final ServerComponent serverComponent;

  @Inject
  HttpRequestProcessor(ServerComponent serverComponent) {
    this.serverComponent = serverComponent;
  }

  /**
   * Sets up the request scope and runs the appropriate handler for the request.
   */
  HttpResponse processRequest(HttpRequest request) {
    // TODO(dino): Figure out if there is a way to create the handler mapping once and reuse it
    // for all requests.

    HttpRequestComponent requestComponent = ((WithRequestScope<?>) serverComponent)
        .requestScopedComponent(new HttpRequestModule(request));
    HttpHandlerMap handlers = requestComponent.handlerMap();

    String path = request.uri().getPath();
    Optional<HandlerMetadata> handler = handlers.resolve(path, request.method());
    if (!handler.isPresent()) {
      return statusResponse(HttpResponseStatus.NOT_FOUND);
    }

    try {
      // TODO(dino): Avoid this nasty casting by having a dedicated handler type for http.
      return (HttpResponse) handler.get().createHandler().handle();
    } catch (Throwable t) {
      // If any goes wrong during processing, the handler should deal with it. If anything leaks to
      // here, then we default to returning a 500.
      logger.error("Caught error while executing http handler", t);
      return statusResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private static HttpResponse statusResponse(final HttpResponseStatus status) {
    return new HttpResponse() {
      @Override
      public HttpResponseStatus status() {
        return status;
      }

      @Override
      public String body() {
        return status.toString();
      }
    };
  }
}
