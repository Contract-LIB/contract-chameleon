package org.contract_lib.lang.contract_lib.label;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.HashSet;
import java.util.IdentityHashMap;

import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;

/// Simple store to associate each AST node with a specific label.
public class LabelStore<T> {

  public LabelStore() {
    // Needs to use an `IdentityHashMap` as we don't want to overwrite all equals def for the AST records.
    this(new IdentityHashMap<>());
  }

  private LabelStore(Map<ContractLibAstElement, T> map) {
    this.elementToLabelMap = map;
  }

  private Map<ContractLibAstElement, T> elementToLabelMap;

  public void putLabel(ContractLibAstElement element, T label) {
    elementToLabelMap.put(element, label);
  }

  public T getLabel(ContractLibAstElement element) {
    return elementToLabelMap.get(element);
  }

  public Set<Entry<ContractLibAstElement, T>> getEntries() {
    return new HashSet<>(elementToLabelMap.entrySet());
  }

  public Set<ContractLibAstElement> getKeys() {
    return new HashSet<>(elementToLabelMap.keySet());
  }
}
