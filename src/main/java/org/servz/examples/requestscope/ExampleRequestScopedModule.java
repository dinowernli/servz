package org.servz.examples.requestscope;

import java.util.UUID;

import org.servz.http.component.HttpRequestScoped;

import dagger.Module;
import dagger.Provides;

/**
 * A module which demonstrates how to add custom request-scoped bindings.
 */
@Module(includes = Servz_HelloWorldHandlerModule.class)
public class ExampleRequestScopedModule {
  @Provides
  @HttpRequestScoped
  UUID provideRequestId() {
    return UUID.randomUUID();
  }
}
