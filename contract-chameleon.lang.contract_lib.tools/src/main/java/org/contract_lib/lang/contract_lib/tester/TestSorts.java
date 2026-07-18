package org.contract_lib.lang.contract_lib.tester;

import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Supplier;

import org.contract_lib.contract_chameleon.adapters.CheckerAdapter.CheckerAdapterResult;
import org.contract_lib.contract_chameleon.contexts.MessageContext;
import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;
import org.contract_lib.lang.contract_lib.contexts.AvailableSortIdentifierContext;
import org.contract_lib.lang.contract_lib.contexts.CurrentFileIdentifierContext;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.AccessSortIdentifierContext;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.DefinedSortIdentifierContext;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.FilePositionLinkerContext;
import org.contract_lib.lang.contract_lib.error.IdentifierError;
import org.contract_lib.lang.contract_lib.label.Identifier;
import org.contract_lib.lang.contract_lib.label.IdentifierMode.Accessed;
import org.contract_lib.lang.contract_lib.label.IdentifierMode.Defined;
import org.contract_lib.lang.contract_lib.label.IdentifierScope.Global;
import org.contract_lib.lang.contract_lib.label.IdentifierScope.Local;
import org.contract_lib.lang.contract_lib.label.IdentifierType.SortIdentifier;

public class TestSorts {

  private final MessageContext messageContext;

  private FilePositionLinkerContext parentLinkerContext;
  private CurrentFileIdentifierContext fileIdentifierContext;

  private DefinedSortIdentifierContext definedSortIdentifierContext;
  private AccessSortIdentifierContext accSortIdentifierExtractor;
  private AvailableSortIdentifierContext availableSortIdentifierContext;

  //TODO: Add static symbols that can appear as identifer, remove from extractors.
  // private final … theory sorts

  public TestSorts(
      MessageContext messageContext) {
    this.messageContext = messageContext;
  }

  public CheckerAdapterResult testSorts(
      CurrentFileIdentifierContext fileIdentifierContext,
      FilePositionLinkerContext parentLinkerContext,
      DefinedSortIdentifierContext availableSortIdentifierContext,
      AccessSortIdentifierContext accessSortIdentifierContext) {

    this.parentLinkerContext = parentLinkerContext;
    this.fileIdentifierContext = fileIdentifierContext;

    this.accSortIdentifierExtractor = accessSortIdentifierContext;
    this.definedSortIdentifierContext = availableSortIdentifierContext;

    List<Supplier<CheckerAdapterResult>> variableChecks = List.of(
        this::testVariableAccess,
        this::testVariableDef);

    return variableChecks
        .stream()
        .map(Supplier::get)
        .reduce(CheckerAdapterResult.SUCCESS,
            CheckerAdapterResult::and);
  }

  /// Using the extreactors of this class, test that all variable definitions are valid (they shadow nothing).
  private CheckerAdapterResult testVariableDef() {
    return definedSortIdentifierContext.getExtractor()
        .allEntries()
        .stream()
        .map(this::checkAstSortDef)
        .reduce(CheckerAdapterResult.SUCCESS,
            CheckerAdapterResult::and);
  }

  /** 
   * Check for an node defining variable identifiers if they are valid,
   * logs error if this is not so.
   */
  private CheckerAdapterResult checkAstSortDef(
      Entry<ContractLibAstElement, Identifier<Defined, Global, SortIdentifier>> entry) {
    ContractLibAstElement node = entry.getKey();

    Set<String> definedIdentifiers = entry.getValue().identifier();

    return definedIdentifiers.stream()
        .map((i) -> {
          if (availableSortIdentifierContext.availableSortIdentifier(node).identifier().contains(i)) {
            messageContext.logReporotable(
                new IdentifierError(
                    IdentifierError.IdentifierErrorKind.REDEFINED,
                    i,
                    new SortIdentifier(),
                    node,
                    parentLinkerContext,
                    fileIdentifierContext));
            return CheckerAdapterResult.FAILURE;
          } else {
            return CheckerAdapterResult.SUCCESS;
          }
        })
        .reduce(CheckerAdapterResult.SUCCESS,
            CheckerAdapterResult::and);
  }

  /// Using the extreactors of this class, test that all variable accesses are valid, the variable is defined.
  private CheckerAdapterResult testVariableAccess() {
    return accSortIdentifierExtractor
        .getNodesAccessingSortIdentifier()
        .stream()
        .map(this::checkAstNodeVariableAccess)
        .reduce(CheckerAdapterResult.SUCCESS,
            CheckerAdapterResult::and);
  }

  /// Check an entry if all identifer  are available and logs error if not so.
  private CheckerAdapterResult checkAstNodeVariableAccess(
      Entry<ContractLibAstElement, Identifier<Accessed, Local, SortIdentifier>> entry) {
    ContractLibAstElement node = entry.getKey();
    Set<String> accessedIdentifiers = entry.getValue().identifier();

    return accessedIdentifiers
        .stream()
        .map((i) -> {
          if (availableSortIdentifierContext.availableSortIdentifier(node).identifier().contains(i)) {
            return CheckerAdapterResult.SUCCESS;
          } else {
            messageContext.logReporotable(
                new IdentifierError(
                    IdentifierError.IdentifierErrorKind.UNDEFINED,
                    i,
                    new SortIdentifier(),
                    node,
                    parentLinkerContext,
                    fileIdentifierContext));
            return CheckerAdapterResult.FAILURE;
          }
        })
        .reduce(CheckerAdapterResult.SUCCESS,
            CheckerAdapterResult::and);
  }
}
