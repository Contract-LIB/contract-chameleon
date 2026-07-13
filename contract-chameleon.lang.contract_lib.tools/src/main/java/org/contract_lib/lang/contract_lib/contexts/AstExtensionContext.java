package org.contract_lib.lang.contract_lib.contexts;

import org.contract_lib.contract_chameleon.SharedContextManager.SharedContext;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstTranslatorExtension;

public interface AstExtensionContext<T extends ContractLibAstTranslatorExtension> extends SharedContext {
  public T getTranslatorExtension();
}
