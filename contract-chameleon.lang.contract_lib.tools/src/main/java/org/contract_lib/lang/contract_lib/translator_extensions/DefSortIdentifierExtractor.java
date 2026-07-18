
package org.contract_lib.lang.contract_lib.translator_extensions;

import java.util.Set;

import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser.Cmd_declareAbstractionContext;
import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser.Cmd_declareDatatypeContext;
import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser.Cmd_declareSortContext;
import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser.Sort_decContext;
import org.contract_lib.lang.contract_lib.ast.Abstraction;
import org.contract_lib.lang.contract_lib.ast.Datatype;
import org.contract_lib.lang.contract_lib.ast.SortDec;
import org.contract_lib.lang.contract_lib.label.Identifier;
import org.contract_lib.lang.contract_lib.label.IdentifierMode.Defined;
import org.contract_lib.lang.contract_lib.label.IdentifierScope.Global;
import org.contract_lib.lang.contract_lib.label.IdentifierType.SortIdentifier;

public final class DefSortIdentifierExtractor extends IdentifierExtractor<Defined, Global, SortIdentifier> {

  @Override
  public void extensionCmdDeclareSort(SortDec res, Cmd_declareSortContext ctx) {
    String id = res.name().identifier();
    Identifier<Defined, Global, SortIdentifier> ad = new Identifier<>(Set.of(id));

    System.err.println("CMD S " + id);
    addToStore(res, ad);
  }

  @Override
  public void extensionCmdDeclareDatatype(Datatype res, Cmd_declareDatatypeContext ctx) {
    String id = res.identifier().name().identifier();
    Identifier<Defined, Global, SortIdentifier> ad = new Identifier<>(Set.of(id));
    System.err.println("CMD D " + id);
    addToStore(res, ad);
  }

  @Override
  public void extensionCmdDeclareAbstraction(Abstraction res, Cmd_declareAbstractionContext ctx) {
    String id = res.identifier().name().identifier();
    Identifier<Defined, Global, SortIdentifier> ad = new Identifier<>(Set.of(id));
    System.err.println("CMD A " + id);
    addToStore(res, ad);
  }

  @Override
  public final void extensionSortDec(SortDec res, Sort_decContext ctx) {
    String id = res.name().identifier();
    Identifier<Defined, Global, SortIdentifier> ad = new Identifier<>(Set.of(id));
    System.err.println("SD " + id);
    addToStore(res, ad);
  }
}
