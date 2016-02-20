package org.servz.compiler;

import java.lang.reflect.Method;

/**
 * Utilities used in generated code. Makes the actual generated code more concise.
 */
public class MethodUtil {
  public static Method getMethodOrThrow(Class<?> clazz, String name, Class<?>... parameterTypes) {
    try {
      return clazz.getDeclaredMethod(name, parameterTypes);
    } catch (NoSuchMethodException | SecurityException e) {
      // Rethrowing this as a runtime exception allows use inside dagger provides methods.
      throw new RuntimeException(e);
    }
  }
}
