package org.contract_lib.lang.contract_lib.tester;

import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Supplier;

import org.contract_lib.contract_chameleon.SharedContextManager;
import org.contract_lib.contract_chameleon.adapters.CheckerAdapter.CheckerAdapterResult;
import org.contract_lib.contract_chameleon.contexts.MessageContext;
import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.AvailableVariableIdentifierContext;
import org.contract_lib.lang.contract_lib.label.Identifier;
import org.contract_lib.lang.contract_lib.label.IdentifierMode.Accessed;
import org.contract_lib.lang.contract_lib.label.IdentifierMode.Defined;
import org.contract_lib.lang.contract_lib.label.IdentifierScope.Local;
import org.contract_lib.lang.contract_lib.label.IdentifierType.VariableIdentifier;
import org.contract_lib.lang.contract_lib.translator_extensions.AccVariableIdentifierExtractor;
import org.contract_lib.lang.contract_lib.translator_extensions.DefVariableIdentifierExtractor;

public class TestVariableScopes {

  private final MessageContext messageContext;

  private AccVariableIdentifierExtractor accVariableIdentifierExtractor;
  private DefVariableIdentifierExtractor defVariableIdentifierExtractor;
  //TODO: Add static symbols that can appear as identifer, remove from extractors.
  // private final … constantIdentifiers

  private AvailableVariableIdentifierContext availableVariableIdentifierContext;

  public TestVariableScopes(MessageContext messageContext) {
    this.messageContext = messageContext;
  }

  public CheckerAdapterResult testVaraibleScope(
      SharedContextManager sharedContextManager) {

    //accVariableIdentifierExtractor = //TODO: Load from shared context
    //availableVariableIdentifierContext = //TODO: Load from shared context

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
    return defVariableIdentifierExtractor
        .allEntries()
        .stream()
        .map(this::checkAstNodeVariableDef)
        .reduce(CheckerAdapterResult.SUCCESS,
            CheckerAdapterResult::and);
  }

  /** 
   * Check for an node defining variable identifiers if they are valid,
   * logs error if this is not so.
   */
  private CheckerAdapterResult checkAstNodeVariableDef(
      Entry<ContractLibAstElement, Identifier<Defined, Local, VariableIdentifier>> entry) {
    ContractLibAstElement node = entry.getKey();

    Set<String> availableIdentifiers = availableVariableIdentifierContext.availableVariableIdentifier(node)
        .identifier();
    Set<String> definedIdentifiers = entry.getValue().identifier();

    return definedIdentifiers.stream()
        .map((i) -> {
          if (availableIdentifiers.contains(i)) {
            return CheckerAdapterResult.SUCCESS;
          } else {

            //TODO: Better log message
            messageContext.logError(String.format("Identifier '%s' is redefined at node: ", i, node));
            return CheckerAdapterResult.FAILURE;
          }
        })
        .reduce(CheckerAdapterResult.SUCCESS,
            CheckerAdapterResult::and);
  }

  /// Using the extreactors of this class, test that all variable accesses are valid, the variable is defined.
  private CheckerAdapterResult testVariableAccess() {
    return accVariableIdentifierExtractor
        .allEntries()
        .stream()
        .map(this::checkAstNodeVariableAccess)
        .reduce(CheckerAdapterResult.SUCCESS,
            CheckerAdapterResult::and);
  }

  /// Check an entry if all identifer  are available and logs error if not so.
  private CheckerAdapterResult checkAstNodeVariableAccess(
      Entry<ContractLibAstElement, Identifier<Accessed, Local, VariableIdentifier>> entry) {
    ContractLibAstElement node = entry.getKey();
    Set<String> availableIdentifiers = availableVariableIdentifierContext.availableVariableIdentifier(node)
        .identifier();
    Set<String> accessedIdentifiers = entry.getValue().identifier();

    return accessedIdentifiers
        .stream()
        .map((i) -> {
          if (availableIdentifiers.contains(i)) {
            return CheckerAdapterResult.SUCCESS;
          } else {

            //TODO: Better log message
            messageContext.logError(String.format("Identifier '%s' not available at node: ", i, node));
            return CheckerAdapterResult.FAILURE;
          }
        })
        .reduce(CheckerAdapterResult.SUCCESS,
            CheckerAdapterResult::and);
  }
}
