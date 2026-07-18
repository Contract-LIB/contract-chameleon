package org.contract_lib.lang.contract_lib.contexts;

import java.util.List;
import java.util.stream.Stream;

import org.contract_lib.contract_chameleon.SharedContextManager.SharedContext;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstTranslatorExtension;

public interface AstExtensionContext extends SharedContext {
  /// Implement this method if you do not override {@code AstExtensionContext::getTranslatorExtensionStream}.
  public List<ContractLibAstTranslatorExtension> getTranslatorExtension();

  default public Stream<ContractLibAstTranslatorExtension> getTranslatorExtensionStream() {
    return this.getTranslatorExtension().stream();
  }
}
