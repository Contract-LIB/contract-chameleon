package org.contract_lib.lang.contract_lib.contexts;

import java.util.List;

import org.contract_lib.contract_chameleon.SharedContextManager.SharedContext;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstTranslatorExtension;

public interface AstExtensionContext extends SharedContext {
  public List<ContractLibAstTranslatorExtension> getTranslatorExtension();
}
