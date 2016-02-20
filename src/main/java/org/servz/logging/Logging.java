package org.servz.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logging {
  public static Logger getLoggerForClass() {
    String className = new Exception().getStackTrace()[1].getClassName();
    String simpleClassName = className.substring(className.lastIndexOf(".") + 1);
    return LoggerFactory.getLogger(simpleClassName);
  }
}
