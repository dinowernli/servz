package org.servz.examples.requestscope;

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

/**
 * The top-level component for our server.
 */
class ExampleServer {
  // TODO(dino): Generate this class from a list of modules and a list of request-scoped modules.

  // TODO(dino): Alternatively, leave this as-is but go through the possible failure modes of what
  // can be forgotten and add some validation checks to the codegen.

  public static ExampleServerComponent createComponent() {
    return DaggerExampleServer_ExampleServerComponent.builder().build();
  }

  @Singleton
  @Component(modules = {ServerModule.class, HttpModule.class, ExampleModule.class})
  public static abstract class ExampleServerComponent extends ServerComponent
      implements WithRequestScope<RequestComponent> {
    @Override
    public abstract RequestComponent requestScopedComponent(HttpRequestModule module);
  }

  @HttpRequestScoped
  @Subcomponent(modules = {HttpRequestModule.class, ExampleRequestScopedModule.class})
  static interface RequestComponent extends HttpRequestComponent {
  }
}