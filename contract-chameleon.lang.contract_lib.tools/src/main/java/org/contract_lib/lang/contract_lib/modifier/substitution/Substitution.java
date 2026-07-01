package org.contract_lib.lang.contract_lib.modifier.substitution;

import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;

@FunctionalInterface
public interface Substitution<Element extends ContractLibAstElement> {

  /// Substitute the provided element with the substitution.
  Element substitue(Element e);
}
