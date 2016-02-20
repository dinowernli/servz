package org.servz.http.netty;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.servz.core.ServerComponent;
import org.servz.handler.Handler;
import org.servz.handler.HandlerMetadata;
import org.servz.http.component.HttpHandlerMap;
import org.servz.http.component.HttpRequest;
import org.servz.http.component.HttpRequestComponent;
import org.servz.http.component.HttpRequestModule;
import org.servz.http.component.HttpResponse;
import org.servz.http.component.WithRequestScope;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;

/** Unit tests for {@link HttpRequestProcessor}. */
public class HttpRequestProcessorTest {
  private static final HttpMethod REQUEST_METHOD = HttpMethod.GET;
  private static final String REQUEST_URI = "http://example.com/foo";

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock private ServerComponentWithRequestScope mockServerComponent;
  @Mock private HttpRequestComponent mockHttpRequestComponent;
  @Mock private HttpRequest mockHttpRequest;
  @Mock private HttpHandlerMap mockHttpHandlerMap;
  @Mock private HandlerMetadata mockHandlerMetadata;
  @Mock private Handler mockHandler;
  @Mock private HttpResponse mockHttpResponse;

  private HttpRequestProcessor httpRequestProcessor;

  @Before public void setUp() throws Throwable {
    when(mockHttpRequest.method()).thenReturn(REQUEST_METHOD);
    when(mockHttpRequest.uri()).thenReturn(new URI(REQUEST_URI));

    when(mockServerComponent.requestScopedComponent(any(HttpRequestModule.class)))
        .thenReturn(mockHttpRequestComponent);
    when(mockHttpRequestComponent.handlerMap()).thenReturn(mockHttpHandlerMap);
    when(mockHandlerMetadata.createHandler()).thenReturn(mockHandler);

    httpRequestProcessor = new HttpRequestProcessor(mockServerComponent);
  }

  @Test public void handlesMissingHandler() {
    when(mockHttpHandlerMap.resolve(any(String.class), any(HttpMethod.class)))
        .thenReturn(Optional.<HandlerMetadata>empty());
    HttpResponse response = httpRequestProcessor.processRequest(mockHttpRequest);
    assertThat(response.status()).isEqualTo(HttpResponseStatus.NOT_FOUND);
  }

  @Test public void handlesCrashingHandler() {
    when(mockHandler.handle()).thenThrow(new RuntimeException());
    when(mockHttpHandlerMap.resolve(any(String.class), any(HttpMethod.class)))
        .thenReturn(Optional.of(mockHandlerMetadata));

    HttpResponse response = httpRequestProcessor.processRequest(mockHttpRequest);
    assertThat(response.status()).isEqualTo(HttpResponseStatus.INTERNAL_SERVER_ERROR);
  }

  @Test public void handlesSuccessfulHandler() {
    when(mockHandler.handle()).thenReturn(mockHttpResponse);
    when(mockHttpHandlerMap.resolve(any(String.class), any(HttpMethod.class)))
        .thenReturn(Optional.of(mockHandlerMetadata));
    when(mockHttpResponse.status()).thenReturn(HttpResponseStatus.MOVED_PERMANENTLY);

    HttpResponse response = httpRequestProcessor.processRequest(mockHttpRequest);
    assertThat(response.status()).isEqualTo(HttpResponseStatus.MOVED_PERMANENTLY);
  }

  abstract class ServerComponentWithRequestScope extends ServerComponent
      implements WithRequestScope<HttpRequestComponent> {
  }
}
