package org.contract_lib.lang.contract_lib.contexts;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.contract_lib.contract_chameleon.SharedContextManager.SharedContext;
import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;
import org.contract_lib.lang.contract_lib.label.Identifier;
import org.contract_lib.lang.contract_lib.label.LabelStore;
import org.contract_lib.lang.contract_lib.label.IdentifierMode.Available;
import org.contract_lib.lang.contract_lib.label.IdentifierMode.Defined;
import org.contract_lib.lang.contract_lib.label.IdentifierScope.Children;
import org.contract_lib.lang.contract_lib.label.IdentifierScope.Global;
import org.contract_lib.lang.contract_lib.label.IdentifierScope.Local;
import org.contract_lib.lang.contract_lib.label.IdentifierType.FunctionIdentifier;
import org.contract_lib.lang.contract_lib.label.IdentifierType.SortIdentifier;
import org.contract_lib.lang.contract_lib.label.IdentifierType.VariableIdentifier;
import org.contract_lib.lang.contract_lib.translator_extensions.IdentifierExtractor;
import org.contract_lib.lang.contract_lib.translator_extensions.ParentLinker;

public class AvailableIdentifierContext implements SharedContext {

  //private final IdentifierAddedExtractor<Global, SortIdentifier> sortIdentifers;
  //private final IdentifierAddedExtractor<Global, FunctionIdentifer> functionIdentifiers;
  private final ParentLinker parentLinker;

  private final IdentifierExtractor<Defined, Local, VariableIdentifier> variableIdentifers;

  private final Identifier<Available, Global, FunctionIdentifier> funcIds;
  private final Identifier<Available, Global, SortIdentifier> sortIds;

  private final LabelStore<Identifier<Available, Children, VariableIdentifier>> varIds;

  public AvailableIdentifierContext(
      ContractLibAstContext astContext,
      IdentifierExtractor<Defined, Global, SortIdentifier> sortIdentifers,
      IdentifierExtractor<Defined, Local, VariableIdentifier> variableIdentifers,
      IdentifierExtractor<Defined, Global, FunctionIdentifier> functionIdentifiers,
      ParentLinker parentLinker) {

    //this.sortIdentifers = sortIdentifers;
    //this.functionIdentifiers = functionIdentifiers;
    this.variableIdentifers = variableIdentifers;
    this.parentLinker = parentLinker;

    this.funcIds = new Identifier<>(functionIdentifiers.allIdentifer().identifier());
    this.sortIds = new Identifier<>(sortIdentifers.allIdentifer().identifier());
    this.varIds = new LabelStore<>();
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

  private final Identifier<Available, Children, VariableIdentifier> mergeAddedWithParentsAvailableIdentifiers(
      ContractLibAstElement astElement) {

    Identifier<Available, Children, VariableIdentifier> v = parentLinker.getParent(astElement)
        .map(p -> availableVariableIdentifier(p))
        .orElseGet(() -> new Identifier<>(Set.of()));
    Set<String> ids = new HashSet<>();
    ids.addAll(v.identifier());
    this.variableIdentifers.getAddedIdentifer(astElement)
        .map(Identifier::identifier)
        .ifPresent(ids::addAll);
    ;

    Identifier<Available, Children, VariableIdentifier> av = new Identifier<>(ids);
    varIds.putLabel(astElement, av);
    return av;
  }

}
