package org.contract_lib.lang.contract_lib.modifier.substitution;

import org.contract_lib.contract_chameleon.error.ChameleonMessageManager;
import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;

@FunctionalInterface
public interface SubstitutionConflict<Element extends ContractLibAstElement> {

  /**
  * @param e the element that triggers the conflict.
  * @param messageManager the message manager the error should be reported to.
  */
  void substitutionConflict(Element e, ChameleonMessageManager messageManager);
}
