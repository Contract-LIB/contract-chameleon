package org.contract_lib.lang.contract_lib.contexts.ast_extensions;

import java.util.List;

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
}
