
package org.contract_lib.lang.contract_lib.contexts;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.contract_lib.contract_chameleon.SharedContextManager;
import org.contract_lib.contract_chameleon.SharedContextManager.SettableContext;
import org.contract_lib.lang.contract_lib.context_provider.AstExtensionContextProvider;

public class AppliedAstExtensionsContext implements SettableContext {

  private final Set<AstExtensionContext> extensionProvider;
  private final SharedContextManager sharedContextManager;

  public Set<AstExtensionContext> getExtensionProviderContexts() {
    return new HashSet<>(extensionProvider);
  }

  public AppliedAstExtensionsContext(SharedContextManager sharedContextManager) {
    this.sharedContextManager = sharedContextManager;
    this.extensionProvider = new HashSet<>();
  }

  public void addAstExtensions(List<AstExtensionContextProvider<? extends AstExtensionContext>> provider) {
    provider
        .stream()
        .forEach(this::addAstExtension);
  }

  public void addAstExtension(AstExtensionContextProvider<? extends AstExtensionContext> provider) {
    sharedContextManager
        .getContext(provider)
        .ifPresent(extensionProvider::add);
  }
}
