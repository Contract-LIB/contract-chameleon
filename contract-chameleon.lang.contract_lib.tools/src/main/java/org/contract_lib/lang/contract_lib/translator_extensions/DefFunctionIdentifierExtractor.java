
package org.contract_lib.lang.contract_lib.translator_extensions;

import java.util.Set;

import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser.Cmd_declareFunContext;
import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser.Selector_decContext;
import org.contract_lib.lang.contract_lib.ast.FunctionDec;
import org.contract_lib.lang.contract_lib.ast.SelectorDec;
import org.contract_lib.lang.contract_lib.label.Identifier;
import org.contract_lib.lang.contract_lib.label.IdentifierMode.Defined;
import org.contract_lib.lang.contract_lib.label.IdentifierScope.Global;
import org.contract_lib.lang.contract_lib.label.IdentifierType.FunctionIdentifier;

public final class DefFunctionIdentifierExtractor extends IdentifierExtractor<Defined, Global, FunctionIdentifier> {

  @Override
  public void extensionSelector(SelectorDec res, Selector_decContext ctx) {
    String id = res.symbol().identifier();
    Identifier<Defined, Global, FunctionIdentifier> ad = new Identifier<>(Set.of(id));
    addToStore(res, ad);
  }

  @Override
  public void extensionCmdDeclareFun(FunctionDec res, Cmd_declareFunContext ctx) {
    String id = res.name().identifier();
    Identifier<Defined, Global, FunctionIdentifier> ad = new Identifier<>(Set.of(id));
    addToStore(res, ad);
  }

}
