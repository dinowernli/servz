package org.servz.http.netty;

import javax.inject.Inject;
import javax.inject.Provider;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * An initializer for the channels spawned by the server socket channel. The
 * {@link #initChannel(SocketChannel)} method is called once per incoming connection to create
 * the child connection.
 */
class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {
  private final Provider<NettyRequestHandler> handlerFactory;

  @Inject
  NettyChannelInitializer(Provider<NettyRequestHandler> handlerFactory) {
    this.handlerFactory = handlerFactory;
  }

  @Override
  public void initChannel(SocketChannel channel) throws Exception {
    ChannelPipeline pipeline = channel.pipeline();
    pipeline.addLast(new HttpRequestDecoder());
    pipeline.addLast(new HttpResponseEncoder());

    // Create a new handler for each channel.
    pipeline.addLast(handlerFactory.get());
  }
}