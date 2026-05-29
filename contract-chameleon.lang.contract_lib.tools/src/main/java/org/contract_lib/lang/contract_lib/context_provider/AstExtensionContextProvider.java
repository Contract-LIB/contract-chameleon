
package org.contract_lib.lang.contract_lib.context_provider;

import org.contract_lib.contract_chameleon.SharedContextManager;
import org.contract_lib.contract_chameleon.SharedContextManager.SharedContextProvider;
import org.contract_lib.lang.contract_lib.contexts.AstExtensionContext;

//NOTE:Only supports one AST at a time at the moment 
public class AstExtensionContextProvider
    implements SharedContextProvider<AstExtensionContext> {

  public AstExtensionContextProvider() {
  }

  @Override
  public AstExtensionContext createContext(SharedContextManager sharedContextManager) {
    return new AstExtensionContext();
  }

  @Override
  public Class<AstExtensionContext> getContext() {
    return AstExtensionContext.class;
  }
}
