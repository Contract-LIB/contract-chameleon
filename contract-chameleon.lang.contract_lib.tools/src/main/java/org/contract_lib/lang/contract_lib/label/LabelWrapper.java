package org.contract_lib.lang.contract_lib.label;

import java.util.Map;
import java.util.HashMap;

//import java.util.IdentityHashMap;

import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;

public class LabelWrapper<T> {

  //TODO: Write proper javadoc

  //TODO: Remove alternative constructor
  /* This initializer might only be necessary,
   *  when the equals method of the key is changed.
   */
  /*
  public static <T> LabelWrapper<T> withIdentityMap() {
    return new LabelWrapper(new IdentityHashMap());
  }
  */

  public LabelWrapper() {
    this(new HashMap<>());
  }

  private LabelWrapper(Map<ContractLibAstElement, T> map) {
    this.elementToLabelMap = map;
  }

  private Map<ContractLibAstElement, T> elementToLabelMap;

  public void putLabel(ContractLibAstElement element, T label) {
    elementToLabelMap.put(element, label);
  }

  public T getLabel(ContractLibAstElement element) {
    return elementToLabelMap.get(element);
  }
}
