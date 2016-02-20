package org.servz.core;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.servz.core.ServerModule.ServerBootstrapInfo;
import org.servz.handler.Annotations.Shutdown;
import org.servz.handler.Annotations.Startup;
import org.servz.handler.HandlerMetadata;
import org.servz.logging.Logging;
import org.slf4j.Logger;

@Singleton
public class Server {
  private static final Logger logger = Logging.getLoggerForClass();
  private final ServerBootstrapInfo serverBootstrapInfo;
  private final Set<HandlerMetadata> startupHandlers;
  private final Set<HandlerMetadata> shutdownHandlers;

  @Inject
  Server(
      ServerBootstrapInfo serverBootstrapInfo,
      @Startup Set<HandlerMetadata> startupHandlers,
      @Shutdown Set<HandlerMetadata> shutdownHandlers) {
    this.serverBootstrapInfo = serverBootstrapInfo;
    this.startupHandlers = startupHandlers;
    this.shutdownHandlers = shutdownHandlers;
  }

  void start(ServerComponent component) {
    logger.info("Starting Servz server");
    serverBootstrapInfo.initialize(component);
    runAll(startupHandlers);
  }

  void stop() {
    logger.info("Stopping Servz server");
    runAll(shutdownHandlers);
  }

  private void runAll(Set<HandlerMetadata> handlers) {
    // TODO(dino): Run these handlers on a different executor to make sure start() and stop()
    // async calls. Right now, we rely on the contract that startup handlers don't block.

    for (HandlerMetadata handlerInfo : handlers) {
      logger.info("Running handler " + handlerInfo.name());
      try {
        handlerInfo.createHandler().handle();
      } catch (Throwable t) {
        logger.error("Error running handler " + handlerInfo.name(), t);
      }
    }
  }
}