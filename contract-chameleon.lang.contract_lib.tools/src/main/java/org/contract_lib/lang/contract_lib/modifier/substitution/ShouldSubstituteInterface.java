package org.contract_lib.lang.contract_lib.modifier.substitution;

import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;

@FunctionalInterface
public interface ShouldSubstituteInterface<Element extends ContractLibAstElement> {

  /// @return {@code true} if the element should be substituted, otherwise {@code false}.
  boolean shouldSubstitute(Element e);
}
