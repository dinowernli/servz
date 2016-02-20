package org.servz.examples.http;

import org.servz.handler.Annotations.HandlerModule;
import org.servz.handler.Annotations.Handles;
import org.servz.handler.Annotations.Startup;
import org.servz.logging.Logging;
import org.slf4j.Logger;

@HandlerModule
public class HelloStartupModule {
  private static final Logger logger = Logging.getLoggerForClass();

  @Handles
  @Startup
  void handleStartup() {
    logger.info("Try it out! http://localhost:12345/hello?u=Servz");
  }
}
