package org.servz.compiler;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import jdk.nashorn.internal.ir.annotations.Immutable;

/** A utility class which provides structured access to a type's name. */
@Immutable
class TypeName {
  private final String packageName;
  private final String simpleName;

  static TypeName fromTypeElement(TypeElement typeElement) {
    if (typeElement.getEnclosingElement().getKind() != ElementKind.PACKAGE) {
      throw new IllegalArgumentException("Type element parsing is not supported for nested types");
    }
    String simpleName = typeElement.getSimpleName().toString();
    String qualifiedName = typeElement.getQualifiedName().toString();
    return new TypeName(
        qualifiedName.substring(0, qualifiedName.lastIndexOf(simpleName) - 1),
        simpleName);
  }

  TypeName(String packageName, String simpleName) {
    this.packageName = packageName;
    this.simpleName = simpleName;
  }

  String packageName() {
    return packageName;
  }

  String simpleName() {
    return simpleName;
  }

  String canonicalName() {
    return packageName + "." + simpleName;
  }
}
