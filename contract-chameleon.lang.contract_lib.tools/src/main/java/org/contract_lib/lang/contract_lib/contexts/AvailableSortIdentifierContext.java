package org.contract_lib.lang.contract_lib.contexts;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.contract_lib.contract_chameleon.SharedContextManager.SharedContext;
import org.contract_lib.lang.contract_lib.ast.Command;
import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.CommandOrderContext;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.DefinedSortIdentifierContext;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.ParentLinkerContext;
import org.contract_lib.lang.contract_lib.label.Identifier;
import org.contract_lib.lang.contract_lib.label.IdentifierMode.Available;
import org.contract_lib.lang.contract_lib.label.IdentifierMode.Defined;
import org.contract_lib.lang.contract_lib.label.IdentifierScope.Global;
import org.contract_lib.lang.contract_lib.label.IdentifierScope.Local;
import org.contract_lib.lang.contract_lib.label.IdentifierType.SortIdentifier;
import org.contract_lib.lang.contract_lib.tools.ChildrenLinker;
import org.contract_lib.lang.contract_lib.translator_extensions.DefSortIdentifierExtractor;
import org.contract_lib.lang.contract_lib.label.LabelStore;

/// An context which holds information what sort identifier are available (defined).
public class AvailableSortIdentifierContext implements SharedContext {

  private final DefinedSortIdentifierContext defSortIdentifierExtractor;
  private final CommandOrderContext commandOrderContext;
  private final ParentLinkerContext parentLinkerContext;
  private final ChildrenLinker childrenLinker;

  private final LabelStore<Identifier<Available, Local, SortIdentifier>> availableIds;

  public AvailableSortIdentifierContext(
      DefinedSortIdentifierContext definedSortIdentifierContext,
      CommandOrderContext commandOrderContext,
      ParentLinkerContext parentLinkerContext) {
    this.defSortIdentifierExtractor = definedSortIdentifierContext;
    this.commandOrderContext = commandOrderContext;
    this.parentLinkerContext = parentLinkerContext;
    this.childrenLinker = new ChildrenLinker();
    availableIds = new LabelStore<>();
  }

  public final Identifier<Available, Local, SortIdentifier> availableSortIdentifier(
      ContractLibAstElement astElement) {
    return Optional.ofNullable(this.availableIds.getLabel(astElement))
        .orElseGet(() -> createAvailableIds(astElement));
  }

  private final Identifier<Available, Local, SortIdentifier> createAvailableIds(
      ContractLibAstElement astElement) {

    Optional<Command> previousCommand = parentLinkerContext
        .getCommand(astElement)
        .flatMap(commandOrderContext::predecessor);

    //TODO: Add identifer defined by default theories
    //Identifier<Available, Local, SortIdentifier>
    Stream<String> previousAvailable = previousCommand
        .map(this::availableSortIdentifier)
        .map(Identifier::identifier)
        .map(Set::stream)
        .orElse(Stream.of());

    //Identifier<Available, Local, SortIdentifier> addedByPreviousCommand = 
    Stream<String> addedByPreviousCommand = previousCommand
        .map(this::getIdentiferDefinedByCommand)
        .map(Identifier::identifier)
        .map(Set::stream)
        .orElse(Stream.of());

    Set<String> createdIds = Stream.concat(previousAvailable, addedByPreviousCommand)
        .collect(Collectors.toSet());

    Identifier<Available, Local, SortIdentifier> av = new Identifier<>(createdIds);
    availableIds.putLabel(astElement, av);
    return av;
  }

  private final Identifier<Available, Local, SortIdentifier> getIdentiferDefinedByCommand(Command command) {
    DefSortIdentifierExtractor extractor = defSortIdentifierExtractor.getExtractor();

    Set<String> ids = childrenLinker
        .getAllNodes(command)
        .map(extractor::getAddedIdentifer)
        .flatMap(this::idsFromIdentifier)
        .collect(Collectors.toSet());

    //TODO: Make available to body of Datatype / Abstraction (rec. definitions!)

    return new Identifier<>(ids);
  }

  private final Stream<String> idsFromIdentifier(Optional<Identifier<Defined, Global, SortIdentifier>> identifier) {
    return identifier.map(Identifier::identifier)
        .map(Set::stream)
        .orElse(Stream.of());
  }

  @Override
  public String toString() {
    return String.format("Available Sort Identifier Context [%n%s%n]",
        availableIds
            .getEntries()
            .stream()
            .map((e) -> String.format("  %s -> %s", e.getKey(), e.getValue().identifier()))
            .collect(Collectors.joining(System.lineSeparator())));
  }

}
