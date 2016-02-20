package org.servz.examples.hello;

import javax.inject.Singleton;

import org.servz.core.ServerComponent;
import org.servz.core.ServerModule;

import dagger.Component;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    HelloServerComponent.createComponent().startServer();
  }

  @Singleton
  @Component(modules = {
    ServerModule.class,
    Servz_HelloHandlerModule.class,
  })
  static abstract class HelloServerComponent extends ServerComponent {
    public static HelloServerComponent createComponent() {
      return DaggerMain_HelloServerComponent.builder().build();
    }
  }
}
