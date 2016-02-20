package org.servz.http.component;

/**
 * An interface used for server component support http request scopes.
 */
public interface WithRequestScope<C extends HttpRequestComponent> {
  /**
   * Returns a new http request scoped subcomponent of this component.
   */
  public abstract C requestScopedComponent(HttpRequestModule module);
}
