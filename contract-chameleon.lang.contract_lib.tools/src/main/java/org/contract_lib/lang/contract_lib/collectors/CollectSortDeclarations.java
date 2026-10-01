package org.contract_lib.lang.contract_lib.collectors;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.ast.SortDec;
import org.contract_lib.lang.contract_lib.ast.Datatype;
import org.contract_lib.contract_chameleon.contexts.MessageContext;
import org.contract_lib.lang.contract_lib.ast.Abstraction;

public final class CollectSortDeclarations {

  Map<String, List<SortDec>> sortDeclarations = new HashMap<>();
  MessageContext messageContext;

  public CollectSortDeclarations(MessageContext messageContext) {
    this.messageContext = messageContext;
  }

  public Set<SortDec> getDeclarations() {
    return new HashSet<>(
        sortDeclarations
            .entrySet()
            .stream()
            .map(Entry::getValue)
            .map(List::getFirst)
            .toList());
  }

  public void collectFrom(ContractLibAst ast) {
    ast.sorts().stream().forEach(this::collectSort);
    ast.datatypes().stream().forEach(this::collectDatatype);
    ast.abstractions().stream().forEach(this::collectAbstraction);
  }

  //TODO: Refactor to shared interface declaring sort

  void collectSort(SortDec sort) {
    String identifier = sort.name().identifier();
    List<SortDec> list = sortDeclarations.getOrDefault(identifier, new ArrayList<>());
    list.add(sort);
    sortDeclarations.put(identifier, list);
  }

  void collectDatatype(Datatype datatype) {
    String identifier = datatype.identifier().name().identifier();
    List<SortDec> list = sortDeclarations.getOrDefault(identifier, new ArrayList<>());
    list.add(datatype.identifier());
    sortDeclarations.put(identifier, list);
  }

  void collectAbstraction(Abstraction abstraction) {
    String identifier = abstraction.identifier().name().identifier();
    List<SortDec> list = sortDeclarations.getOrDefault(identifier, new ArrayList<>());
    list.add(abstraction.identifier());
    sortDeclarations.put(identifier, list);
  }

  void check() {
    sortDeclarations.values().stream().forEach(this::testIdentifier);
  }

  void testIdentifier(List<SortDec> sortDeclarations) {
    if (sortDeclarations.size() > 1) {
      //TODO: report error / ill definition 
    }
  }
}
