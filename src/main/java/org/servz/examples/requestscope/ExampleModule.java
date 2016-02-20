package org.servz.examples.requestscope;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ExampleModule {
  @Provides
  @Singleton
  HttpResponseUtil provideResponseUtil() {
    return new HttpResponseUtil();
  }
}
