
package org.contract_lib.lang.contract_lib.context_provider;

import org.contract_lib.contract_chameleon.SharedContextManager;
import org.contract_lib.contract_chameleon.SharedContextManager.SharedContextProvider;
import org.contract_lib.lang.contract_lib.contexts.AppliedAstExtensionsContext;

public class AppliedAstExtensionsContextProvider implements SharedContextProvider<AppliedAstExtensionsContext> {

  @Override
  public Class<AppliedAstExtensionsContext> getContext() {
    return AppliedAstExtensionsContext.class;
  }

  @Override
  public AppliedAstExtensionsContext createContext(SharedContextManager sharedContextManager) {
    return new AppliedAstExtensionsContext(sharedContextManager);
  }
}
