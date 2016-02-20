package org.servz.http.netty;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.inject.Provider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/** Unit tests for {@link NettyChannelInitializer}. */
public class NettyChannelInitializerTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock private Provider<NettyRequestHandler> mockRequestHandlerProvider;
  @Mock private SocketChannel mockSocketChannel;
  @Mock private ChannelPipeline mockChannelPipeline;

  private NettyChannelInitializer nettyChannelInitializer;

  @Before public void setUp() {
    when(mockSocketChannel.pipeline()).thenReturn(mockChannelPipeline);

    nettyChannelInitializer = new NettyChannelInitializer(mockRequestHandlerProvider);
  }

  @Test public void usesNewHandlerForEveryRequest() throws Throwable {
    nettyChannelInitializer.initChannel(mockSocketChannel);
    nettyChannelInitializer.initChannel(mockSocketChannel);
    verify(mockRequestHandlerProvider, times(2)).get();
  }
}
