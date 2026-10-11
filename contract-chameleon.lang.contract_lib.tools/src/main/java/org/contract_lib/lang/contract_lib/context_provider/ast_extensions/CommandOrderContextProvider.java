package org.contract_lib.lang.contract_lib.context_provider.ast_extensions;

import org.contract_lib.contract_chameleon.SharedContextManager;
import org.contract_lib.lang.contract_lib.context_provider.AstExtensionContextProvider;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.CommandOrderContext;

public class CommandOrderContextProvider
    implements AstExtensionContextProvider<CommandOrderContext> {

  public CommandOrderContextProvider() {
  }

  @Override
  public CommandOrderContext createContext(SharedContextManager sharedContextManager) {
    return new CommandOrderContext();
  }

  @Override
  public Class<CommandOrderContext> getContext() {
    return CommandOrderContext.class;
  }
}
