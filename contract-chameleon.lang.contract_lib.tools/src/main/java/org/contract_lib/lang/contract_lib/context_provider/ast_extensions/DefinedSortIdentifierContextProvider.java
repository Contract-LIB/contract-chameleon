
package org.contract_lib.lang.contract_lib.context_provider.ast_extensions;

import org.contract_lib.contract_chameleon.SharedContextManager;
import org.contract_lib.lang.contract_lib.context_provider.AstExtensionContextProvider;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.DefinedSortIdentifierContext;

public class DefinedSortIdentifierContextProvider
    implements AstExtensionContextProvider<DefinedSortIdentifierContext> {

  public DefinedSortIdentifierContextProvider() {
  }

  @Override
  public DefinedSortIdentifierContext createContext(SharedContextManager sharedContextManager) {
    return new DefinedSortIdentifierContext();
  }

  @Override
  public Class<DefinedSortIdentifierContext> getContext() {
    return DefinedSortIdentifierContext.class;
  }
}
