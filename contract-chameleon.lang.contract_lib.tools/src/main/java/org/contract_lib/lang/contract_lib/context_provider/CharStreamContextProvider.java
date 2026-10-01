
package org.contract_lib.lang.contract_lib.context_provider;

import java.io.IOException;
import java.util.Optional;

import org.antlr.v4.runtime.CharStreams;
import org.contract_lib.contract_chameleon.SharedContextManager;
import org.contract_lib.contract_chameleon.SharedContextManager.SharedContextProvider;
import org.contract_lib.lang.contract_lib.contexts.CharStreamContext;

public class CharStreamContextProvider implements SharedContextProvider<CharStreamContext> {

  @Override
  public Class<CharStreamContext> getContext() {
    return CharStreamContext.class;
  }

  @Override
  public CharStreamContext createContext(SharedContextManager sharedContextManager) {
    try {
      return new CharStreamContext(Optional.of(CharStreams.fromPath(
          sharedContextManager
              .getContext(new FileIdentifierContextProvider())
              .get()
              .getFilePath())));

    } catch (IOException exception) {
      sharedContextManager.getMessageContext().logException(exception);
      return new CharStreamContext(Optional.empty());
    }

  }
}
