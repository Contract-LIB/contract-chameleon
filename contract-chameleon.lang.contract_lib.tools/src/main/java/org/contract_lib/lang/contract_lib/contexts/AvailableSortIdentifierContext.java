package org.contract_lib.lang.contract_lib.contexts;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.contract_lib.contract_chameleon.SharedContextManager.SharedContext;
import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.DefinedSortIdentifierContext;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.ParentLinkerContext;
import org.contract_lib.lang.contract_lib.label.Identifier;
import org.contract_lib.lang.contract_lib.label.IdentifierMode.Available;
import org.contract_lib.lang.contract_lib.label.IdentifierScope.Children;
import org.contract_lib.lang.contract_lib.label.IdentifierType.SortIdentifier;
import org.contract_lib.lang.contract_lib.label.LabelStore;

/// An context which holds information what sort identifier are available (defined).
public class AvailableSortIdentifierContext implements SharedContext {

  private final DefinedSortIdentifierContext defSortIdentifierExtractor;
  //private final CommandOrderContext commandOrderContext;
  private final ParentLinkerContext parentLinkerContext;

  private final LabelStore<Identifier<Available, Children, SortIdentifier>> varIds;

  public AvailableSortIdentifierContext(
      DefinedSortIdentifierContext definedSortIdentifierContext,
      //CommandOrderContext commandOrderContext,
      ParentLinkerContext parentLinkerContext) {
    this.defSortIdentifierExtractor = definedSortIdentifierContext;
    //this.commandOrderContext = commandOrderContext;
    this.parentLinkerContext = parentLinkerContext;
    varIds = new LabelStore<>();
  }

  public final Identifier<Available, Children, SortIdentifier> availableSortIdentifier(
      ContractLibAstElement astElement) {
    return Optional.ofNullable(this.varIds.getLabel(astElement))
        .orElseGet(() -> mergeAddedWithParentsAvailableIdentifiers(astElement));
  }

  private final Identifier<Available, Children, SortIdentifier> mergeAddedWithParentsAvailableIdentifiers(
      ContractLibAstElement astElement) {

    Identifier<Available, Children, SortIdentifier> v = parentLinkerContext.getParent(astElement)
        .map(p -> availableSortIdentifier(p))
        .orElseGet(() -> new Identifier<>(Set.of()));
    Set<String> ids = new HashSet<>();
    ids.addAll(v.identifier());
    this.defSortIdentifierExtractor
        .getExtractor()
        .getAddedIdentifer(astElement)
        .map(Identifier::identifier)
        .ifPresent(ids::addAll);

    Identifier<Available, Children, SortIdentifier> av = new Identifier<>(ids);
    varIds.putLabel(astElement, av);
    return av;
  }

  /*
  public Optional<Identifier<Defined, Local, SortIdentifier>> getAvailableIdentifierFor() {
    return defSortIdentifierExtractor.allIdentifier();
  }
  public final Identifier<Available, Global, FunctionIdentifier> availableFunctionIdentifier() {
    return this.funcIds;
  }
  
  public final Identifier<Available, Global, SortIdentifier> availableSortIdentifier() {
    return this.sortIds;
  }
  
  public final Identifier<Available, Children, VariableIdentifier> availableVariableIdentifier(
      ContractLibAstElement astElement) {
    return Optional.ofNullable(this.varIds.getLabel(astElement))
        .orElseGet(() -> mergeAddedWithParentsAvailableIdentifiers(astElement));
  }
  */
}
