package org.contract_lib.lang.contract_lib.contexts.ast_extensions;

import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;
import org.contract_lib.lang.contract_lib.contexts.AstExtensionContext;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstTranslatorExtension;
import org.contract_lib.lang.contract_lib.label.Identifier;
import org.contract_lib.lang.contract_lib.label.IdentifierMode.Accessed;
import org.contract_lib.lang.contract_lib.label.IdentifierScope.Local;
import org.contract_lib.lang.contract_lib.label.IdentifierType.SortIdentifier;
import org.contract_lib.lang.contract_lib.translator_extensions.AccSortIdentifierExtractor;

/// An context which holds information about the nodes that access a sort identifier.
public class AccessSortIdentifierContext implements AstExtensionContext {

  private final AccSortIdentifierExtractor accSortIdentifierExtractor;

  public AccessSortIdentifierContext() {
    this.accSortIdentifierExtractor = new AccSortIdentifierExtractor();
  }

  @Override
  public List<ContractLibAstTranslatorExtension> getTranslatorExtension() {
    return List.of(accSortIdentifierExtractor);
  }

  public Set<Entry<ContractLibAstElement, Identifier<Accessed, Local, SortIdentifier>>> getNodesAccessingSortIdentifier() {
    return accSortIdentifierExtractor.allEntries();
  }
}
