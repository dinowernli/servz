package org.servz.integration;

import javax.inject.Singleton;

import org.junit.Before;
import org.junit.Test;
import org.servz.core.ServerComponent;
import org.servz.core.ServerModule;

import dagger.Component;

public class LifeCycleTest {
  private ServerComponent component;

  @Before
  public void setUp() {
    component = DaggerLifeCycleTest_EmptyServerComponent.builder().build();
  }

  @Test
  public void runsWithoutCrashing() {
    component.startServer();
    component.stopServer();
  }

  @Singleton
  @Component(modules = ServerModule.class)
  static abstract class EmptyServerComponent extends ServerComponent {
  }
}

