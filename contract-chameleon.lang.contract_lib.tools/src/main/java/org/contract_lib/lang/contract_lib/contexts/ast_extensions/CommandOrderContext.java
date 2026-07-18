package org.contract_lib.lang.contract_lib.contexts.ast_extensions;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.contract_lib.lang.contract_lib.ast.Command;
import org.contract_lib.lang.contract_lib.contexts.AstExtensionContext;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstTranslatorExtension;
import org.contract_lib.lang.contract_lib.translator_extensions.CommandOrderExtractor;

/// An context providing the order in which the commands are called.
public class CommandOrderContext implements AstExtensionContext {

  private final CommandOrderExtractor commandOrderExtractor;

  public CommandOrderContext() {
    this.commandOrderExtractor = new CommandOrderExtractor();
  }

  public List<ContractLibAstTranslatorExtension> getTranslatorExtension() {
    return List.of(this.commandOrderExtractor);
  }

  public Stream<Command> getCommands() {
    return commandOrderExtractor.getCommands();
  }

  public Optional<Command> predecessor(Command command) {
    return commandOrderExtractor.predecessor(command);
  }

  public Optional<Command> successor(Command command) {
    return commandOrderExtractor.successor(command);
  }

  @Override
  public String toString() {
    return commandOrderExtractor.toString()
        + System.lineSeparator()
        + System.lineSeparator()
        + getCommands()
            .map(this::predecessor)
            .map((o) -> String.format("%s", o)).collect(Collectors.joining(System.lineSeparator()))
        + System.lineSeparator()
        + System.lineSeparator()
        + getCommands()
            .map(this::successor)
            .map((o) -> String.format("%s", o)).collect(Collectors.joining(System.lineSeparator()));
  }
}
