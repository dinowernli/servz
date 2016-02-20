package org.servz.core;

/**
 * A component which can be used to run a single {@link Server}. Used to bootstrap the Dagger2
 * dependency injection machinery.
 */
public abstract class ServerComponent {
  public abstract Server server();

  /**
   * Starts the server by running all startup handlers synchronously.
   */
  public void startServer() {
    server().start(this);
  }

  /**
   * Stops the server by running all shutdown handlers synchronously.
   */
  public void stopServer() {
    server().stop();
  }
}