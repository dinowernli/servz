package org.servz.http;

import javax.inject.Singleton;

import org.servz.http.Annotations.NettyServer;
import org.servz.http.netty.NettyHttpServer;

import dagger.Module;
import dagger.Provides;

@Module(includes = Servz_HttpServerHandlerModule.class)
public class HttpModule {
  @Provides
  @NettyServer
  @Singleton
  NettyHttpServer provideNettyHttpServer(NettyHttpServer server) {
    return server;
  }
}
