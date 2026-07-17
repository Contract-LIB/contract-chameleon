package org.contract_lib.lang.contract_lib.contexts.ast_extensions;

import java.util.List;

import org.contract_lib.lang.contract_lib.contexts.AstExtensionContext;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstTranslatorExtension;
import org.contract_lib.lang.contract_lib.label.Identifier;
import org.contract_lib.lang.contract_lib.label.IdentifierMode.Defined;
import org.contract_lib.lang.contract_lib.label.IdentifierScope.Total;
import org.contract_lib.lang.contract_lib.label.IdentifierType.SortIdentifier;
import org.contract_lib.lang.contract_lib.translator_extensions.DefSortIdentifierExtractor;

/// An context which holds information what sort identifier are available.
public class AvailableSortIdentifierContext implements AstExtensionContext {

  private final DefSortIdentifierExtractor defSortIdentifierExtractor;
  //TODO: Load sort identifier from defined grammars
  //private final …

  public AvailableSortIdentifierContext() {
    this.defSortIdentifierExtractor = new DefSortIdentifierExtractor();
  }

  @Override
  public List<ContractLibAstTranslatorExtension> getTranslatorExtension() {
    return List.of(defSortIdentifierExtractor);
  }

  public Identifier<Defined, Total, SortIdentifier> allIdentifier() {
    return defSortIdentifierExtractor.allIdentifer();
  }
}
