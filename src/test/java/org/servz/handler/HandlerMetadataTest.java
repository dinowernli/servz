package org.servz.handler;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Provider;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.servz.handler.Annotations.Handles;
import org.servz.handler.Annotations.Trigger;

/** Unit tests for @link{HandlerMetadata}. */
public class HandlerMetadataTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock private Provider<Handler> mockHandlerProvider;

  @Test public void happyCase() throws Throwable {
    HandlerMetadata metadata = setupWithMethod("goodHandlesMethod");
    assertThat(metadata.hasAnnotation(SomeAnnotation.class)).isTrue();
    assertThat(metadata.getAnnotation(SomeAnnotation.class).isPresent()).isTrue();
    assertThat(metadata.name()).isEqualTo("HandlerMetadataTest.goodHandlesMethod");

    assertThat(metadata.hasAnnotation(OtherAnnotation.class)).isFalse();
    assertThat(metadata.getAnnotation(OtherAnnotation.class).isPresent()).isFalse();

    metadata.createHandler();
    verify(mockHandlerProvider).get();
  }

  @Test(expected = IllegalArgumentException.class)
  public void validatesHandlesPresent() throws Throwable {
    setupWithMethod("handlerMethodWithoutHandles");
  }

  public void handlerMethodWithoutHandles() {
  }

  @Handles
  @SomeAnnotation
  public void goodHandlesMethod() {
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Trigger public @interface SomeAnnotation {}

  @Retention(RetentionPolicy.RUNTIME)
  @Trigger public @interface OtherAnnotation {}

  private HandlerMetadata setupWithMethod(String method) throws Throwable {
    return new HandlerMetadata(HandlerMetadataTest.class.getMethod(method), mockHandlerProvider);
  }
}
