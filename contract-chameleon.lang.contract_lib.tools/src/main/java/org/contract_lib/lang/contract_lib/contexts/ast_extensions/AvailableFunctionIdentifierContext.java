package org.contract_lib.lang.contract_lib.contexts.ast_extensions;

import java.util.List;

import org.contract_lib.lang.contract_lib.contexts.AstExtensionContext;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstTranslatorExtension;
import org.contract_lib.lang.contract_lib.label.Identifier;
import org.contract_lib.lang.contract_lib.label.IdentifierMode.Defined;
import org.contract_lib.lang.contract_lib.label.IdentifierScope.Total;
import org.contract_lib.lang.contract_lib.label.IdentifierType.FunctionIdentifier;
import org.contract_lib.lang.contract_lib.translator_extensions.DefFunctionIdentifierExtractor;

/// An context which holds information what function identifier are available.
public class AvailableFunctionIdentifierContext implements AstExtensionContext {

  private final DefFunctionIdentifierExtractor defFunctionIdentifierExtractor;
  //TODO: Load sort identifier from defined grammars
  //private final …

  public AvailableFunctionIdentifierContext() {
    this.defFunctionIdentifierExtractor = new DefFunctionIdentifierExtractor();
  }

  @Override
  public List<ContractLibAstTranslatorExtension> getTranslatorExtension() {
    return List.of(defFunctionIdentifierExtractor);
  }

  public Identifier<Defined, Total, FunctionIdentifier> getAvailableIdentifierFor() {
    return defFunctionIdentifierExtractor.allIdentifier();
  }
}
