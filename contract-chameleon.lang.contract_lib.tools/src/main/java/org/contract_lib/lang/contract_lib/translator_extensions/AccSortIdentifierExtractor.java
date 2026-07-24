package org.contract_lib.lang.contract_lib.translator_extensions;

import java.util.Set;

import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser.FormalContext;
import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser.Selector_decContext;
import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser.Sorted_varContext;
import org.contract_lib.lang.contract_lib.ast.Formal;
import org.contract_lib.lang.contract_lib.ast.SelectorDec;
import org.contract_lib.lang.contract_lib.ast.SortedVar;
import org.contract_lib.lang.contract_lib.label.Identifier;
import org.contract_lib.lang.contract_lib.label.IdentifierMode.Accessed;
import org.contract_lib.lang.contract_lib.label.IdentifierScope.Local;
import org.contract_lib.lang.contract_lib.label.IdentifierType.SortIdentifier;

public final class AccSortIdentifierExtractor extends IdentifierExtractor<Accessed, Local, SortIdentifier> {

  @Override
  public void extensionFormal(Formal res, FormalContext ctx) {
    String id = res.sort().getName();
    Identifier<Accessed, Local, SortIdentifier> i = new Identifier<>(Set.of(id));
    this.addToStore(res, i);
  }

  @Override
  public void extensionSortedVar(SortedVar res, Sorted_varContext ctx) {
    String id = res.sort().getName();
    Identifier<Accessed, Local, SortIdentifier> i = new Identifier<>(Set.of(id));
    this.addToStore(res, i);
  }

  @Override
  public void extensionSelector(SelectorDec res, Selector_decContext ctx) {
    String id = res.sort().getName();
    Identifier<Accessed, Local, SortIdentifier> i = new Identifier<>(Set.of(id));
    this.addToStore(res, i);
  }

  //TODO: Add Cmd Define Sort
}
