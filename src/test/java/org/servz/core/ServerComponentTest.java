package org.servz.core;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/** Unit tests for {@link ServerComponent}. */
public class ServerComponentTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock private Server mockServer;

  private ServerComponent serverComponent;

  @Before public void setUp() {
    serverComponent = new ServerComponentImpl();
  }

  @Test public void startsServer() {
    serverComponent.startServer();
    verify(mockServer).start(serverComponent);
  }

  @Test public void stopsServer() {
    serverComponent.stopServer();
    verify(mockServer).stop();
  }

  private class ServerComponentImpl extends ServerComponent {
    @Override
    public Server server() {
      return mockServer;
    }
  }
}
