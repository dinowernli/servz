package org.servz.handler;

public interface Handler {
  // TODO(dino): Distinguish between different kinds of handlers. Each type (startup, http,
  // grpc, etc.,) shoud just have their own interface.
  Object handle();
}
