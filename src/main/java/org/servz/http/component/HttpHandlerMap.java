package org.servz.http.component;

import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.servz.handler.HandlerMetadata;
import org.servz.http.Annotations.Get;
import org.servz.http.Annotations.HttpRequest;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;

import io.netty.handler.codec.http.HttpMethod;

/**
 * A class which keeps track of the handlers bound to http endpoints.
 */
public class HttpHandlerMap {
  private final ImmutableMap<HandlerKey, HandlerMetadata> handlerMap;

  @Inject
  @Singleton
  HttpHandlerMap(@HttpRequest Set<HandlerMetadata> handlers) {
    ImmutableMap.Builder<HandlerKey, HandlerMetadata> mapBuilder = ImmutableMap.builder();
    for (HandlerMetadata handler : handlers) {
      Optional<Get> getAnnotation = handler.getAnnotation(Get.class);
      if (getAnnotation.isPresent()) {
        String path = getAnnotation.get().path();
        mapBuilder.put(HandlerKey.create(path, HttpMethod.GET), handler);
      }
    }
    this.handlerMap = mapBuilder.build();
  }

  /** Returns the method to be executed for the specified path and method. */
  public Optional<HandlerMetadata> resolve(String path, HttpMethod method) {
    return Optional.ofNullable(handlerMap.get(HandlerKey.create(path, method)));
  }

  @AutoValue
  static abstract class HandlerKey {
    abstract String path();
    abstract HttpMethod method();

    static HandlerKey create(String path, HttpMethod method) {
      return new AutoValue_HttpHandlerMap_HandlerKey(path, method);
    }
  }
}
