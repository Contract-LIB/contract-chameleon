package org.contract_lib.lang.contract_lib.translator_extensions;

import java.util.Set;

import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser.FormalContext;
import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser.IdentifierContext;
import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser.Sorted_varContext;
import org.contract_lib.lang.contract_lib.ast.Formal;
import org.contract_lib.lang.contract_lib.ast.SortedVar;
import org.contract_lib.lang.contract_lib.ast.Term.Identifier.IdentifierValue;
import org.contract_lib.lang.contract_lib.label.Identifier;
import org.contract_lib.lang.contract_lib.label.IdentifierMode.Accessed;
import org.contract_lib.lang.contract_lib.label.IdentifierScope.Local;
import org.contract_lib.lang.contract_lib.label.IdentifierType.VariableIdentifier;

public final class AccVariableIdentifierExtractor extends IdentifierExtractor<Accessed, Local, VariableIdentifier> {

  //TODO: to implement

  @Override
  public void extensionFormal(Formal res, FormalContext ctx) {
    String id = res.identifier().identifier();
    Identifier<Accessed, Local, VariableIdentifier> i = new Identifier<>(Set.of(id));
    this.addToStore(res, i);
  }

  @Override
  public void extensionSortedVar(SortedVar res, Sorted_varContext ctx) {
    String id = res.symbol().identifier();
    Identifier<Accessed, Local, VariableIdentifier> i = new Identifier<>(Set.of(id));
    this.addToStore(res, i);
  }

  @Override
  public void extendsionTermIdentifierValue(IdentifierValue res, IdentifierContext ctx) {
    String id = res.identifier().identifier();
    Identifier<Accessed, Local, VariableIdentifier> i = new Identifier<>(Set.of(id));
    this.addToStore(res, i);
  }
}
