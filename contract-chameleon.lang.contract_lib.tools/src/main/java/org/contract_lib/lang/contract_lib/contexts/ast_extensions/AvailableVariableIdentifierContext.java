
package org.contract_lib.lang.contract_lib.contexts.ast_extensions;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;
import org.contract_lib.lang.contract_lib.contexts.AstExtensionContext;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstTranslatorExtension;
import org.contract_lib.lang.contract_lib.label.Identifier;
import org.contract_lib.lang.contract_lib.label.LabelStore;
import org.contract_lib.lang.contract_lib.label.IdentifierMode.Available;
import org.contract_lib.lang.contract_lib.label.IdentifierScope.Children;
import org.contract_lib.lang.contract_lib.label.IdentifierType.VariableIdentifier;
import org.contract_lib.lang.contract_lib.translator_extensions.DefVariableIdentifierExtractor;

/// An context which holds information what sort identifier are available.
public class AvailableVariableIdentifierContext implements AstExtensionContext {

  private final DefVariableIdentifierExtractor defVariableIdentifierExtractor;
  private final ParentLinkerContext parentLinker;

  private final LabelStore<Identifier<Available, Children, VariableIdentifier>> varIds;

  public AvailableVariableIdentifierContext(ParentLinkerContext parentLinker) {
    //TODO: Move to own context
    this.defVariableIdentifierExtractor = new DefVariableIdentifierExtractor();

    this.varIds = new LabelStore<>();
    this.parentLinker = parentLinker;
  }

  @Override
  public List<ContractLibAstTranslatorExtension> getTranslatorExtension() {
    return List.of(
        defVariableIdentifierExtractor);
  }

  public final Identifier<Available, Children, VariableIdentifier> availableVariableIdentifier(
      ContractLibAstElement astElement) {
    return Optional.ofNullable(this.varIds.getLabel(astElement))
        .orElseGet(() -> mergeAddedWithParentsAvailableIdentifiers(astElement));
  }

  private final Identifier<Available, Children, VariableIdentifier> mergeAddedWithParentsAvailableIdentifiers(
      ContractLibAstElement astElement) {

    Identifier<Available, Children, VariableIdentifier> v = parentLinker.getParent(astElement)
        .map(p -> availableVariableIdentifier(p))
        .orElseGet(() -> new Identifier<>(Set.of()));
    Set<String> ids = new HashSet<>();
    ids.addAll(v.identifier());
    this.defVariableIdentifierExtractor.getAddedIdentifer(astElement)
        .map(Identifier::identifier)
        .ifPresent(ids::addAll);
    ;

    Identifier<Available, Children, VariableIdentifier> av = new Identifier<>(ids);
    varIds.putLabel(astElement, av);
    return av;
  }

}
