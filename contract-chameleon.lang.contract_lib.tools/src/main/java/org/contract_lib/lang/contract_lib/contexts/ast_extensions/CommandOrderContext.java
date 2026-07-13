package org.contract_lib.lang.contract_lib.contexts.ast_extensions;

import org.contract_lib.lang.contract_lib.contexts.AstExtensionContext;
import org.contract_lib.lang.contract_lib.translator_extensions.CommandOrderExtractor;

/// An context providing the order in which the commands are called.
public class CommandOrderContext implements AstExtensionContext<CommandOrderExtractor> {

  private final CommandOrderExtractor commandOrderExtractor;

  public CommandOrderContext() {
    this.commandOrderExtractor = new CommandOrderExtractor();
  }

  public CommandOrderExtractor getTranslatorExtension() {
    return this.commandOrderExtractor;
  }
}
