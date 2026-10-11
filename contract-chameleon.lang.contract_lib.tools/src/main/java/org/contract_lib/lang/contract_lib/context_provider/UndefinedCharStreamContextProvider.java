
package org.contract_lib.lang.contract_lib.context_provider;

import java.util.Optional;

import org.contract_lib.contract_chameleon.SharedContextManager;
import org.contract_lib.contract_chameleon.SharedContextManager.SharedContextProvider;
import org.contract_lib.lang.contract_lib.contexts.CharStreamContext;

/// This provider creates a {@code CharStreamContext} where the stream needs to be set manually.
///
/// This is needed for testing with ressources.
public class UndefinedCharStreamContextProvider implements SharedContextProvider<CharStreamContext> {

  @Override
  public Class<CharStreamContext> getContext() {
    return CharStreamContext.class;
  }

  @Override
  public CharStreamContext createContext(SharedContextManager sharedContextManager) {
    return new CharStreamContext(Optional.empty());
  }
}
