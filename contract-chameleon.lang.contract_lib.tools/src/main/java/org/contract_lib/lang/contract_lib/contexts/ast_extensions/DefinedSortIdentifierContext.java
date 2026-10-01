package org.contract_lib.lang.contract_lib.contexts.ast_extensions;

import java.util.List;

import org.contract_lib.lang.contract_lib.contexts.AstExtensionContext;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstTranslatorExtension;
import org.contract_lib.lang.contract_lib.translator_extensions.DefSortIdentifierExtractor;

/// An context which holds information what sort identifier are available.
public class DefinedSortIdentifierContext implements AstExtensionContext {

  private final DefSortIdentifierExtractor defSortIdentifierExtractor;

  public DefinedSortIdentifierContext() {
    this.defSortIdentifierExtractor = new DefSortIdentifierExtractor();
  }

  @Override
  public List<ContractLibAstTranslatorExtension> getTranslatorExtension() {
    return List.of(defSortIdentifierExtractor);
  }

  public DefSortIdentifierExtractor getExtractor() {
    return defSortIdentifierExtractor;
  }
}
