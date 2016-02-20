package org.servz.compiler;

/**
 * An exception indicating that generating code failed because the original code was invalid in
 * some way, i.e., because the processed annotations were used in some unexpected way.
 */
class ValidationException extends Exception {
  private static final long serialVersionUID = 1L;

  ValidationException(String message) {
    super(message);
  }
}
