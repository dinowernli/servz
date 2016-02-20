package org.servz.http;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.servz.http.netty.NettyHttpServer;

/** Unit tests for {@link HttpServerHandlerModule}. */
public class HttpServerHandlerModuleTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock private NettyHttpServer mockNettyServer;

  private HttpServerHandlerModule httpServerHandlerModule;

  @Before public void setUp() {
    httpServerHandlerModule = new HttpServerHandlerModule();
  }

  @Test public void startsHttpServerOnStartup() {
    httpServerHandlerModule.startup(mockNettyServer);
    verify(mockNettyServer).start();
  }

  @Test public void stopsHttpServerOnShutdown() throws Throwable {
    httpServerHandlerModule.shutdown(mockNettyServer);
    verify(mockNettyServer).stop();
  }
}
