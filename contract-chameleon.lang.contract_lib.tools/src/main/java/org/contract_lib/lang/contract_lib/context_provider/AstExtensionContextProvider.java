package org.contract_lib.lang.contract_lib.context_provider;

import org.contract_lib.contract_chameleon.SharedContextManager.SharedContextProvider;
import org.contract_lib.lang.contract_lib.contexts.AstExtensionContext;

public interface AstExtensionContextProvider<C extends AstExtensionContext> extends SharedContextProvider<C> {
}
