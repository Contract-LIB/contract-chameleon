package org.contract_lib.lang.contract_lib.label;

import java.util.Set;

/// Interface to access all identifer of a given type that are available at a node.
public interface AvailableIdentifier<T extends IdentifierType> {

  /// The all available identifer of identifer type {@code T}.
  public Set<String> getIdentifier();
}
