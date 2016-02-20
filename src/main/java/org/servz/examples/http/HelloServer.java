package org.servz.examples.http;

import javax.inject.Singleton;

import org.servz.core.ServerComponent;
import org.servz.core.ServerModule;
import org.servz.http.HttpModule;
import org.servz.http.component.HttpRequestComponent;
import org.servz.http.component.HttpRequestModule;
import org.servz.http.component.HttpRequestScoped;
import org.servz.http.component.WithRequestScope;

import dagger.Component;
import dagger.Subcomponent;

class HelloServer {
  @Singleton
  @Component(modules = {
    ServerModule.class,
    HttpModule.class,
    Servz_HelloStartupModule.class,
  })
  static abstract class HelloServerComponent extends ServerComponent
      implements WithRequestScope<HelloServerRequestComponent> {
    public static HelloServerComponent createComponent() {
      return DaggerHelloServer_HelloServerComponent.builder().build();
    }

    @Override
    public abstract HelloServerRequestComponent requestScopedComponent(HttpRequestModule module);
  }

  @HttpRequestScoped
  @Subcomponent(modules = {
    HttpRequestModule.class,
    Servz_HelloHttpHandlerModule.class
  })
  static interface HelloServerRequestComponent extends HttpRequestComponent {
  }
}
