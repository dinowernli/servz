package org.servz.http.netty;

import java.util.Optional;
import java.util.concurrent.ThreadFactory;

import javax.inject.Inject;

import org.servz.logging.Logging;
import org.slf4j.Logger;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * A simple netty http server which installs a single handler for all requests, dispatching to the
 * right servz handler.
 */
public class NettyHttpServer {
  private static final Logger logger = Logging.getLoggerForClass();
  private static final int PORT = 12345;
  private static final int BOSS_THREADS = 1;
  private static final int WORKER_THREADS = 10;
  private static final String BOSS_NAME = "httpserver-boss";
  private static final String WORKER_NAME = "httpserver-worker";

  private final NettyChannelInitializer channelInitializer;

  private Optional<StartedServer> startedServer;

  @Inject
  NettyHttpServer(NettyChannelInitializer channelInitializer) {
    this.channelInitializer = channelInitializer;
    this.startedServer = Optional.empty();
  }

  public void start() {
    Preconditions.checkState(!startedServer.isPresent(), "Http server already started");

    EventLoopGroup bossGroup = new NioEventLoopGroup(BOSS_THREADS, threadsNamed(BOSS_NAME));
    EventLoopGroup workerGroup = new NioEventLoopGroup(WORKER_THREADS, threadsNamed(WORKER_NAME));
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    serverBootstrap
        .group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .childHandler(channelInitializer);

    // Bind the parent channel to the server port (synchronously).
    Channel channel;
    try {
      channel = serverBootstrap.bind(PORT).sync().channel();
    } catch (InterruptedException e) {
      logger.info("Http server nterrupted during bind, shutting down");
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
      return;
    }

    startedServer = Optional.of(new StartedServer(bossGroup, workerGroup, channel));
    logger.info("Http server started on port " + PORT);
  }

  public void stop() throws InterruptedException {
    Preconditions.checkState(startedServer.isPresent(), "Http server not started");
    startedServer.get().channel.close().sync();
    startedServer.get().bossGroup.shutdownGracefully();
    startedServer.get().workerGroup.shutdownGracefully();

    logger.info("Http server stopped");
  }

  private static ThreadFactory threadsNamed(String name) {
    return new ThreadFactoryBuilder()
        .setNameFormat(name + "-%d")
        .build();
  }

  /** The infrastructure which is only available once the server has started. */
  private static class StartedServer {
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup  workerGroup;
    private final Channel channel;

    StartedServer(EventLoopGroup bossGroup, EventLoopGroup workerGroup, Channel channel) {
      this.bossGroup = bossGroup;
      this.workerGroup = workerGroup;
      this.channel = channel;
    }
  }
}
