package org.servz.examples.requestscope;

import org.servz.examples.requestscope.ExampleServer.ExampleServerComponent;

public class Main {
  private static final int LIFETIME_MS = 20_000;

  public static void main(String[] args) throws InterruptedException {
    final ExampleServerComponent serverComponent = ExampleServer.createComponent();

    serverComponent.startServer();
    Thread.sleep(LIFETIME_MS);
    serverComponent.stopServer();
  }
}
