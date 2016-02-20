package org.servz.http.component;

import static com.google.common.truth.Truth.assertThat;

import java.util.Optional;

import javax.inject.Provider;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.servz.handler.Annotations.Handles;
import org.servz.handler.Handler;
import org.servz.handler.HandlerMetadata;
import org.servz.http.Annotations.Get;
import org.servz.http.Annotations.HttpRequest;

import com.google.common.collect.ImmutableSet;

import io.netty.handler.codec.http.HttpMethod;

/** Unit tests for @link{HttpHandlerMap}. */
public class HttpHandlerMapTest {
  private static final String FAKE_PATH = "/fake/path";

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock private Provider<Handler> mockHandlerProvider;

  @Test public void emptyMap() {
    HttpHandlerMap handlerMap = new HttpHandlerMap(ImmutableSet.<HandlerMetadata>of());
    assertThat(handlerMap.resolve(FAKE_PATH, HttpMethod.GET).isPresent()).isFalse();
  }

  @Test public void returnsGetHandler() throws Throwable {
    HttpHandlerMap handlerMap = new HttpHandlerMap(ImmutableSet.of(
        setupWithMethod("fakeHandler")));

    Optional<HandlerMetadata> metadata = handlerMap.resolve(FAKE_PATH, HttpMethod.GET);
    assertThat(metadata.isPresent()).isTrue();
    assertThat(metadata.get().name()).isEqualTo("HttpHandlerMapTest.fakeHandler");
    assertThat(metadata.get().hasAnnotation(Get.class)).isTrue();

    assertThat(handlerMap.resolve(FAKE_PATH, HttpMethod.PATCH).isPresent()).isFalse();
  }

  @Test(expected = IllegalArgumentException.class)
  public void rejectsDuplicates() throws Throwable {
    new HttpHandlerMap(ImmutableSet.of(
        setupWithMethod("fakeHandler"),
        setupWithMethod("duplicateFakeHandler")));
  }

  @Handles
  @HttpRequest
  @Get(path = FAKE_PATH)
  public Object fakeHandler() {
    return null;
  }

  @Handles
  @HttpRequest
  @Get(path = FAKE_PATH)
  public Object duplicateFakeHandler() {
    return null;
  }

  private HandlerMetadata setupWithMethod(String method) throws Throwable {
    return new HandlerMetadata(HttpHandlerMapTest.class.getMethod(method), mockHandlerProvider);
  }
}
