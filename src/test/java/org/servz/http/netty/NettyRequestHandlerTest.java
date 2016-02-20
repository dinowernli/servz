package org.servz.http.netty;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.servz.http.component.HttpResponse;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

/** Unit tests for {@link NettyRequestHandler}. */
public class NettyRequestHandlerTest {
  private static final String REQUEST_URI = "http://example.com/asdf";
  private static final HttpResponseStatus RESPONSE_STATUS = HttpResponseStatus.OK;
  private static final String RESPONSE_BODY = "some body";

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock private HttpRequestProcessor mockRequestProcessor;
  @Mock private ChannelHandlerContext mockChannelHandlerContext;
  @Mock private ChannelFuture mockChannelFuture;
  @Mock private HttpRequest mockHttpRequest;
  @Mock private HttpResponse mockHttpResponse;
  @Captor private ArgumentCaptor<io.netty.handler.codec.http.HttpResponse> httpResponseCaptor;

  private NettyRequestHandler nettyRequestHandler;

  @Before public void setUp() {
    when(mockChannelHandlerContext.writeAndFlush(any())).thenReturn(mockChannelFuture);
    when(mockHttpRequest.getUri()).thenReturn(REQUEST_URI);
    when(mockRequestProcessor.processRequest(any(org.servz.http.component.HttpRequest.class)))
        .thenReturn(mockHttpResponse);
    when(mockHttpResponse.body()).thenReturn(RESPONSE_BODY);
    when(mockHttpResponse.status()).thenReturn(RESPONSE_STATUS);

    nettyRequestHandler = new NettyRequestHandler(mockRequestProcessor);
  }

  @Test public void closesConnection() throws Throwable {
    nettyRequestHandler.channelRead0(mockChannelHandlerContext, mockHttpRequest);
    verify(mockChannelFuture).addListener(ChannelFutureListener.CLOSE);
  }

  @Test public void returnsResponseObtainedFromProcessor() throws Throwable {
    nettyRequestHandler.channelRead0(mockChannelHandlerContext, mockHttpRequest);
    verify(mockChannelHandlerContext).write(httpResponseCaptor.capture());
    assertThat(httpResponseCaptor.getValue().getStatus()).isEqualTo(RESPONSE_STATUS);
  }
}
