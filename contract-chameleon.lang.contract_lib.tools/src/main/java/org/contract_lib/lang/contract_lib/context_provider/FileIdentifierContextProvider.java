package org.contract_lib.lang.contract_lib.context_provider;

import org.contract_lib.contract_chameleon.SharedContextManager;
import org.contract_lib.contract_chameleon.SharedContextManager.SharedContextProvider;
import org.contract_lib.lang.contract_lib.contexts.CurrentFileIdentifierContext;

public class FileIdentifierContextProvider implements SharedContextProvider<CurrentFileIdentifierContext> {

  public final String defaultIdentifier;
  private static final String DEFAULT = "undef";

  public FileIdentifierContextProvider() {
    this(DEFAULT);
  }

  public FileIdentifierContextProvider(String defaultIdentifier) {
    this.defaultIdentifier = defaultIdentifier;
  }

  @Override
  public Class<CurrentFileIdentifierContext> getContext() {
    return CurrentFileIdentifierContext.class;
  }

  @Override
  public CurrentFileIdentifierContext createContext(SharedContextManager sharedContextManager) {
    return new CurrentFileIdentifierContext(defaultIdentifier);
  }
}
