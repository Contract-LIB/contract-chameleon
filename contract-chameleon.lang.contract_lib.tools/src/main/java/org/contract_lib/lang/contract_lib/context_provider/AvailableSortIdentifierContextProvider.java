
package org.contract_lib.lang.contract_lib.context_provider;

import org.contract_lib.contract_chameleon.SharedContextManager;
import org.contract_lib.contract_chameleon.SharedContextManager.SharedContextProvider;
import org.contract_lib.lang.contract_lib.context_provider.AstExtensionContextProvider;
import org.contract_lib.lang.contract_lib.context_provider.ast_extensions.CommandOrderContextProvider;
import org.contract_lib.lang.contract_lib.context_provider.ast_extensions.DefinedSortIdentifierContextProvider;
import org.contract_lib.lang.contract_lib.context_provider.ast_extensions.ParentLinkerContextProvider;
import org.contract_lib.lang.contract_lib.contexts.AvailableSortIdentifierContext;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.DefinedSortIdentifierContext;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.ParentLinkerContext;

public class AvailableSortIdentifierContextProvider
    implements SharedContextProvider<AvailableSortIdentifierContext> {

  public AvailableSortIdentifierContextProvider() {
  }

  @Override
  public AvailableSortIdentifierContext createContext(SharedContextManager sharedContextManager) {
    DefinedSortIdentifierContext definedSortIdentifierContext = sharedContextManager
        .getContext(new DefinedSortIdentifierContextProvider())
        .get();
    ParentLinkerContext parentLinkerContext = sharedContextManager
        .getContext(new ParentLinkerContextProvider())
        .get();
    return new AvailableSortIdentifierContext(
        definedSortIdentifierContext,
        parentLinkerContext);
  }

  @Override
  public Class<AvailableSortIdentifierContext> getContext() {
    return AvailableSortIdentifierContext.class;
  }
}
