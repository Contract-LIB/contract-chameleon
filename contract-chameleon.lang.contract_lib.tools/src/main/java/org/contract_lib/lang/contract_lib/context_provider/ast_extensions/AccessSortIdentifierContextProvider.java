
package org.contract_lib.lang.contract_lib.context_provider.ast_extensions;

import org.contract_lib.contract_chameleon.SharedContextManager;
import org.contract_lib.lang.contract_lib.context_provider.AstExtensionContextProvider;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.AccessSortIdentifierContext;

public class AccessSortIdentifierContextProvider
    implements AstExtensionContextProvider<AccessSortIdentifierContext> {

  public AccessSortIdentifierContextProvider() {
  }

  @Override
  public AccessSortIdentifierContext createContext(SharedContextManager sharedContextManager) {
    return new AccessSortIdentifierContext();
  }

  @Override
  public Class<AccessSortIdentifierContext> getContext() {
    return AccessSortIdentifierContext.class;
  }
}
