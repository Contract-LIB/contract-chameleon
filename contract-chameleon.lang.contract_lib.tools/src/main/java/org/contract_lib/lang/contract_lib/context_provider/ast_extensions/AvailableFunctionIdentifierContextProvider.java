
package org.contract_lib.lang.contract_lib.context_provider.ast_extensions;

import org.contract_lib.contract_chameleon.SharedContextManager;
import org.contract_lib.lang.contract_lib.context_provider.AstExtensionContextProvider;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.AvailableFunctionIdentifierContext;

public class AvailableFunctionIdentifierContextProvider
    implements AstExtensionContextProvider<AvailableFunctionIdentifierContext> {

  public AvailableFunctionIdentifierContextProvider() {
  }

  @Override
  public AvailableFunctionIdentifierContext createContext(SharedContextManager sharedContextManager) {
    return new AvailableFunctionIdentifierContext();
  }

  @Override
  public Class<AvailableFunctionIdentifierContext> getContext() {
    return AvailableFunctionIdentifierContext.class;
  }
}
