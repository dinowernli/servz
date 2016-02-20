package org.servz.compiler;

import com.google.common.collect.ImmutableSet;

public class WriterUtil {
  public String writePackage(String packageName) {
    return writeLine("package " + packageName + ";");
  }

  public String writeImports(ImmutableSet<Class<?>> imports) {
    StringBuilder resultBuilder = new StringBuilder();
    for (Class<?> importClass : imports) {
      resultBuilder.append(writeLine("import " + importClass.getCanonicalName() + ";"));
    }
    return resultBuilder.toString();
  }

  public String writeFinalField(String type, String name) {
    return writeLine("private final " + type + " " + name + ";");
  }

  public String writeMethodCall(String receiver, String methodName, String... argNames) {
    StringBuilder combinedArgs = new StringBuilder();
    boolean first = true;
    for (String argName : argNames) {
      if (!first) {
        combinedArgs.append(", ");
      }
      combinedArgs.append(argName);
      first = false;
    }
    return writeLine(receiver + "." + methodName + "(" + combinedArgs.toString() + ");");
  }

  public String writeLine(String string) {
    return string + "\n";
  }
}
