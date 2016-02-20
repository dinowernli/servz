package org.servz.core;

import java.util.Optional;
import java.util.Set;

import javax.inject.Singleton;

import org.servz.handler.Annotations.Shutdown;
import org.servz.handler.Annotations.Startup;
import org.servz.handler.HandlerMetadata;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import dagger.Module;
import dagger.Provides;

/**
 * A module which provides basic bindings required for a minimal server to work.
 */
@Module
public class ServerModule {
  @Provides(type = Provides.Type.SET_VALUES)
  @Startup
  Set<HandlerMetadata> emptyStartupHandlersBinding() {
    return ImmutableSet.of();
  }

  @Provides(type = Provides.Type.SET_VALUES)
  @Shutdown
  Set<HandlerMetadata> emptyShutdownHandlersBinding() {
    return ImmutableSet.of();
  }

  @Provides
  @Singleton
  ServerBootstrapInfo provideInfo() {
    return new ServerBootstrapInfo();
  }

  @Provides
  ServerComponent provideComponent(ServerBootstrapInfo info) {
    return info.component();
  }

  static class ServerBootstrapInfo {
    private Optional<ServerComponent> component;

    void initialize(ServerComponent component) {
      Preconditions.checkState(!this.component.isPresent(), "Must not be initialized");
      this.component = Optional.of(component);
    }

    private ServerBootstrapInfo() {
      this.component = Optional.empty();
    }

    ServerComponent component() {
      Preconditions.checkState(this.component.isPresent(), "Must be initialized");
      return component.get();
    }

    @VisibleForTesting
    static ServerBootstrapInfo createForTesting() {
      return new ServerBootstrapInfo();
    }
  }
}
