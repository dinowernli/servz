package org.servz.examples.http;

import org.servz.examples.http.HelloServer.HelloServerComponent;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    HelloServerComponent.createComponent().startServer();
  }
}
