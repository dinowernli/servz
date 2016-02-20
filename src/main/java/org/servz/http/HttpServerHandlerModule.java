package org.servz.http;

import org.servz.core.Server;
import org.servz.handler.Annotations.HandlerModule;
import org.servz.handler.Annotations.Handles;
import org.servz.handler.Annotations.Shutdown;
import org.servz.handler.Annotations.Startup;
import org.servz.http.Annotations.NettyServer;
import org.servz.http.netty.NettyHttpServer;
import org.servz.logging.Logging;
import org.slf4j.Logger;

/**
 * A handler module which starts up an http server when the {@link Server} starts up.
 */
@HandlerModule
public class HttpServerHandlerModule {
  private static final Logger logger = Logging.getLoggerForClass();

  @Handles
  @Startup
  void startup(final @NettyServer NettyHttpServer nettyHttpServer) {
    nettyHttpServer.start();
  }

  @Handles
  @Shutdown
  void shutdown(@NettyServer NettyHttpServer nettyHttpServer) {
    try {
      nettyHttpServer.stop();
    } catch (InterruptedException e) {
      logger.warn("Caught interrupt during shutdown. Bailing out");
      Thread.currentThread().interrupt();
    }
  }
}
