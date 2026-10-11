
package org.contract_lib.lang.contract_lib.context_provider.ast_extensions;

import org.contract_lib.contract_chameleon.SharedContextManager;
import org.contract_lib.lang.contract_lib.context_provider.AstExtensionContextProvider;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.FilePositionLinkerContext;

public class FilePositionLinkerContextProvider
    implements AstExtensionContextProvider<FilePositionLinkerContext> {

  public FilePositionLinkerContextProvider() {
  }

  @Override
  public FilePositionLinkerContext createContext(SharedContextManager sharedContextManager) {
    return new FilePositionLinkerContext();
  }

  @Override
  public Class<FilePositionLinkerContext> getContext() {
    return FilePositionLinkerContext.class;
  }
}
