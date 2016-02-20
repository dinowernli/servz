package org.servz.http.component;

import java.net.URI;

import org.servz.http.Annotations.HttpParams;

import com.google.common.collect.ImmutableMap;

import dagger.Module;
import dagger.Provides;
import io.netty.handler.codec.http.HttpMethod;

@Module
public class HttpRequestModule {
  private final HttpRequest httpRequest;

  public HttpRequestModule(HttpRequest httpRequest) {
    this.httpRequest = httpRequest;
  }

  @Provides
  @HttpRequestScoped
  URI provideRequestUri() {
    return httpRequest.uri();
  }

  @Provides
  @HttpRequestScoped
  HttpMethod provideRequestMethod() {
    return httpRequest.method();
  }

  @Provides
  @HttpRequestScoped
  @HttpParams
  ImmutableMap<String, String> provideHttpRequestParams(URI uri) {
    if (uri.getQuery() == null) {
      return ImmutableMap.of();
    }

    ImmutableMap.Builder<String, String> paramsBuilder = ImmutableMap.builder();
    String[] parts = uri.getQuery().split("&");
    for (String part : parts) {
      String[] keyValue = part.split("=", 2);
      if (keyValue.length != 2) {
        continue;
      }
      paramsBuilder.put(keyValue[0], keyValue[1]);
    }
    return paramsBuilder.build();
  }
}
