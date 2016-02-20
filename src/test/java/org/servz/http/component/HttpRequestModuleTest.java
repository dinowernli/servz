package org.servz.http.component;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.google.common.collect.ImmutableMap;

import io.netty.handler.codec.http.HttpMethod;

/** Unit tests for @link{HttpRequestModule}. */
public class HttpRequestModuleTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock private HttpRequest mockHttpRequest;

  private HttpRequestModule httpRequestModule;

  @Before public void setUp() {
    httpRequestModule = new HttpRequestModule(mockHttpRequest);
  }

  @Test public void providesRequestMethod() {
    when(mockHttpRequest.method()).thenReturn(HttpMethod.PUT);
    assertThat(httpRequestModule.provideRequestMethod()).isEqualTo(HttpMethod.PUT);
  }

  @Test public void providesUri() throws Throwable {
    URI uri = new URI("http://example.com");
    when(mockHttpRequest.uri()).thenReturn(uri);
    assertThat(httpRequestModule.provideRequestUri()).isEqualTo(uri);
  }

  @Test public void providesParameterMap() throws Throwable {
    URI uri = new URI("http://example.com?foo=bar");
    when(mockHttpRequest.uri()).thenReturn(uri);

    ImmutableMap<String, String> params = httpRequestModule.provideHttpRequestParams(uri);
    assertThat(params.get("foo")).isEqualTo("bar");
  }

  @Test public void handlesBadQuery() throws Throwable {
    URI uri = new URI("http://example.com??&&(&????*&(*&(*HJHK");
    when(mockHttpRequest.uri()).thenReturn(uri);

    ImmutableMap<String, String> params = httpRequestModule.provideHttpRequestParams(uri);
    assertThat(params.get("foo")).isNull();
  }
}
