package org.contract_lib.lang.contract_lib.collectors;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.ast.SortDec;
import org.contract_lib.lang.contract_lib.ast.Datatype;
import org.contract_lib.lang.contract_lib.ast.Abstraction;

final class CollectSortDeclarations {

  Map<String, List<SortDec>> sortDeclarations = new HashMap<>();

  void collectFrom(ContractLibAst ast) {
    ast.sorts().stream().forEach(this::collectSort);
    ast.datatypes().stream().forEach(this::collectDatatype);
    ast.abstractions().stream().forEach(this::collectAbstraction);
  }

  void collectSort(SortDec.Def sort) {
    String identifier = sort.name().identifier();
    List<SortDec> list = sortDeclarations.getOrDefault(identifier, new ArrayList<>());
    list.add(sort);
    sortDeclarations.put(identifier, list);
  }

  void collectDatatype(Datatype datatype) {

  }

  void collectAbstraction(Abstraction abstraction) {

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
