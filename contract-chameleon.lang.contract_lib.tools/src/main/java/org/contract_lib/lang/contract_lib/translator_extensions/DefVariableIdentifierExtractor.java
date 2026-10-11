
package org.contract_lib.lang.contract_lib.translator_extensions;

import java.util.Set;
import java.util.stream.Collectors;

import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser.Cmd_defineContractContext;
import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser.FormalContext;
import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser.TermContext;
import org.contract_lib.lang.contract_lib.ast.Contract;
import org.contract_lib.lang.contract_lib.ast.Formal;
import org.contract_lib.lang.contract_lib.ast.SortedVar;
import org.contract_lib.lang.contract_lib.ast.Symbol;
import org.contract_lib.lang.contract_lib.ast.VarBinding;
import org.contract_lib.lang.contract_lib.ast.Term.LetBinding;
import org.contract_lib.lang.contract_lib.ast.Term.QuantorBinding;
import org.contract_lib.lang.contract_lib.label.Identifier;
import org.contract_lib.lang.contract_lib.label.IdentifierMode.Defined;
import org.contract_lib.lang.contract_lib.label.IdentifierScope.Local;
import org.contract_lib.lang.contract_lib.label.IdentifierType.VariableIdentifier;

public final class DefVariableIdentifierExtractor extends IdentifierExtractor<Defined, Local, VariableIdentifier> {

  @Override
  public void extensionFormal(Formal res, FormalContext ctx) {
    String id = res.identifier().identifier();
    Identifier<Defined, Local, VariableIdentifier> ad = new Identifier<>(Set.of(id));
    addToStore(res, ad);
  }

  @Override
  public void extensionCmdDefineContract(Contract res, Cmd_defineContractContext ctx) {
    Set<String> ids = res.formals().stream()
        .map(Formal::identifier)
        .map(Symbol::identifier)
        .collect(Collectors.toSet());

    Identifier<Defined, Local, VariableIdentifier> ad = new Identifier<>(ids);
    addToStore(res, ad);
  }

  @Override
  public void extensionTermLetBinding(LetBinding res, TermContext ctx) {
    Set<String> ids = res.varbindings()
        .stream()
        .map(VarBinding::name)
        .map(Symbol::identifier)
        .collect(Collectors.toSet());

    Identifier<Defined, Local, VariableIdentifier> ad = new Identifier<>(ids);
    addToStore(res, ad);
  }

  @Override
  public void extensionTermQuantorBinding(QuantorBinding res, TermContext ctx) {
    Set<String> ids = res.formals().stream()
        .map(SortedVar::symbol)
        .map(Symbol::identifier)
        .collect(Collectors.toSet());

    Identifier<Defined, Local, VariableIdentifier> ad = new Identifier<>(ids);
    addToStore(res, ad);
  }

}
