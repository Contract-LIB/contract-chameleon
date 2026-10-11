
package org.contract_lib.lang.contract_lib.context_provider.ast_extensions;

import org.contract_lib.contract_chameleon.SharedContextManager;
import org.contract_lib.lang.contract_lib.context_provider.AstExtensionContextProvider;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.ParentLinkerContext;

public class ParentLinkerContextProvider
    implements AstExtensionContextProvider<ParentLinkerContext> {

  public ParentLinkerContextProvider() {
  }

  @Override
  public ParentLinkerContext createContext(SharedContextManager sharedContextManager) {
    return new ParentLinkerContext();
  }

  @Override
  public Class<ParentLinkerContext> getContext() {
    return ParentLinkerContext.class;
  }
}
