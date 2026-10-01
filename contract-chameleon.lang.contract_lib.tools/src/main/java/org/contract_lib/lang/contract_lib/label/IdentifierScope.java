package org.contract_lib.lang.contract_lib.label;

public interface IdentifierScope {
  /// Scope starts at the top level of the file.
  public final class Global implements IdentifierScope {
  }

  /// Scope is AST node and all of its children.
  public final class Children implements IdentifierScope {
  }

  /// Scope is only AST node.
  public final class Local implements IdentifierScope {
  }

  /// Scope are all somewhere defined instances.
  public final class Total implements IdentifierScope {
  }
}
