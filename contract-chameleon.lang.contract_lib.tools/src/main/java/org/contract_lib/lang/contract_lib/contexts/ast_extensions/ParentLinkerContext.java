
package org.contract_lib.lang.contract_lib.contexts.ast_extensions;

import java.util.List;
import java.util.Optional;

import org.contract_lib.lang.contract_lib.ast.Command;
import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;
import org.contract_lib.lang.contract_lib.contexts.AstExtensionContext;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstTranslatorExtension;
import org.contract_lib.lang.contract_lib.translator_extensions.ParentLinker;

/// An context which holds information about the parent of each ast node.
public class ParentLinkerContext implements AstExtensionContext {

  private final ParentLinker parentLinker;

  public ParentLinkerContext() {
    this.parentLinker = new ParentLinker();
  }

  @Override
  public List<ContractLibAstTranslatorExtension> getTranslatorExtension() {
    return List.of(this.parentLinker);
  }

  public Optional<ContractLibAstElement> getParent(ContractLibAstElement element) {
    return parentLinker.getParent(element);
  }

  public Optional<Command> getCommand(ContractLibAstElement element) {
    return testCommand(element);
  }

  private Optional<Command> testCommand(ContractLibAstElement element) {
    return element.perform(
        _e -> Optional.empty(),
        this::testJoindedCommand,
        this::findParentCommand,
        this::findParentCommand,
        this::findParentCommand);
  }

  private Optional<Command> findParentCommand(ContractLibAstElement element) {
    return getParent(element)
        .flatMap(this::testCommand);
  }

  private Optional<Command> testJoindedCommand(Command command) {
    return command.perform(
        (c) -> Optional.of(findParentCommand(c).orElse(c)), //decAbstraction,
        Optional::of, //decAbstractions,
        Optional::of, //decConstant,
        (c) -> Optional.of(findParentCommand(c).orElse(c)), //decDatatype,
        Optional::of, //decDatatypes,
        Optional::of, //decFunction,
        Optional::of, //decParameter,
        (c) -> Optional.of(findParentCommand(c).orElse(c)), //decSort,
        Optional::of, //defSort,
        Optional::of, //defContract,
        Optional::of, //defFunction,
        Optional::of, //defFunctionRec,
        (c) -> Optional.of(findParentCommand(c).orElse(c)), //defFunctionsRec,
        Optional::of); //assertion);
  }

  @Override
  public String toString() {
    return parentLinker.toString();
  }
}
