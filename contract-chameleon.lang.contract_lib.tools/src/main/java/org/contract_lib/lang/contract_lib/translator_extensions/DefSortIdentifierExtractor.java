
package org.contract_lib.lang.contract_lib.translator_extensions;

import java.util.Set;

import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser.Sort_decContext;
import org.contract_lib.lang.contract_lib.ast.SortDec;
import org.contract_lib.lang.contract_lib.label.Identifier;
import org.contract_lib.lang.contract_lib.label.IdentifierMode.Defined;
import org.contract_lib.lang.contract_lib.label.IdentifierScope.Global;
import org.contract_lib.lang.contract_lib.label.IdentifierType.SortIdentifier;

public final class DefSortIdentifierExtractor extends IdentifierExtractor<Defined, Global, SortIdentifier> {

  @Override
  public final void extensionSortDec(SortDec res, Sort_decContext ctx) {
    String id = res.name().identifier();
    Identifier<Defined, Global, SortIdentifier> ad = new Identifier<>(Set.of(id));
    addToStore(res, ad);
  }
}
