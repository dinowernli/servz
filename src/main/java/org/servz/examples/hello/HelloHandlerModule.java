package org.servz.examples.hello;

import org.servz.handler.Annotations.HandlerModule;
import org.servz.handler.Annotations.Handles;
import org.servz.handler.Annotations.Startup;
import org.servz.logging.Logging;
import org.slf4j.Logger;

@HandlerModule
class HelloHandlerModule {
  private static final Logger logger = Logging.getLoggerForClass();

  @Handles
  @Startup
  void handleStartup() {
    logger.info("Hello, startup!");
  }
}
