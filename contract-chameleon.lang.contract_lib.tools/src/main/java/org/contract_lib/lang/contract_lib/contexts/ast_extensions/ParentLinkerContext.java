
package org.contract_lib.lang.contract_lib.contexts.ast_extensions;

import java.util.List;
import java.util.Optional;

import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;
import org.contract_lib.lang.contract_lib.contexts.AstExtensionContext;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstTranslatorExtension;
import org.contract_lib.lang.contract_lib.translator_extensions.ParentLinker;

/// An context which holds information about the parent of each ast node.
public class ParentLinkerContext implements AstExtensionContext {

  private final ParentLinker parentLinker;

  public ParentLinkerContext() {
    this.parentLinker = new ParentLinker();
  }

  @Override
  public List<ContractLibAstTranslatorExtension> getTranslatorExtension() {
    return List.of(this.parentLinker);
  }

  public Optional<ContractLibAstElement> getParent(ContractLibAstElement element) {
    return parentLinker.getParent(element);
  }
}
