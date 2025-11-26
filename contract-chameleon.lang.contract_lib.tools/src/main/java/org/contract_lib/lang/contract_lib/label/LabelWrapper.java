package org.contract_lib.lang.contract_lib.label;

import java.util.Map;
import java.util.HashMap;

import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;

/** 
 * Adds a label to the elements of the contract lib ast. 
 *
 * @see ContractLibAstElement
 */
public final class LabelWrapper<T> {

  /// Create a new label wrapper.
  public LabelWrapper() {
    this.elementToLabelMap = new HashMap<>();
  }

  private Map<ContractLibAstElement, T> elementToLabelMap;

  public void putLabel(ContractLibAstElement element, T label) {
    elementToLabelMap.put(element, label);
  }

  public T getLabel(ContractLibAstElement element) {
    return elementToLabelMap.get(element);
  }
}
