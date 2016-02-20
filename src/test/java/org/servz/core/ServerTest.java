package org.servz.core;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.servz.core.ServerModule.ServerBootstrapInfo;
import org.servz.handler.Handler;
import org.servz.handler.HandlerMetadata;

import com.google.common.collect.ImmutableSet;

/** Unit tests for {@link Server}. */
public class ServerTest {
  private Server server;
  private ServerBootstrapInfo serverBootstrapInfo;

  @Rule public MockitoRule mockitoInit = MockitoJUnit.rule();
  @Mock private HandlerMetadata startupHandlerMetadata;
  @Mock private Handler startupHandler;
  @Mock private HandlerMetadata shutdownHandlerMetadata;
  @Mock private Handler shutdownHandler;
  @Mock private ServerComponent serverComponent;

  @Before public void setUp() {
    when(startupHandlerMetadata.createHandler()).thenReturn(startupHandler);
    when(shutdownHandlerMetadata.createHandler()).thenReturn(shutdownHandler);

    serverBootstrapInfo = ServerBootstrapInfo.createForTesting();
    server = new Server(
        serverBootstrapInfo,
        ImmutableSet.of(startupHandlerMetadata),
        ImmutableSet.of(shutdownHandlerMetadata));
  }

  @Test public void serverComponentBootstrappedAfterStartup() {
    server.start(serverComponent);
    assertThat(serverBootstrapInfo.component()).isEqualTo(serverComponent);
  }

  @Test public void callsStartupHandler() {
    server.start(serverComponent);
    verify(startupHandler).handle();
    verifyZeroInteractions(shutdownHandler);
    verifyZeroInteractions(shutdownHandlerMetadata);
  }

  @Test public void handlesThrowingStartupHandler() {
    when(startupHandler.handle()).thenThrow(new RuntimeException("boo!"));
    server.start(serverComponent);
    verify(startupHandler).handle();
  }

  @Test public void callsShutdownHandler() {
    server.stop();
    verify(shutdownHandler).handle();
    verifyZeroInteractions(startupHandler);
    verifyZeroInteractions(startupHandlerMetadata);
  }

  @Test public void handlesThrowingShutdownHandler() {
    when(shutdownHandler.handle()).thenThrow(new RuntimeException("boo!"));
    server.stop();
    verify(shutdownHandler).handle();
  }
}
