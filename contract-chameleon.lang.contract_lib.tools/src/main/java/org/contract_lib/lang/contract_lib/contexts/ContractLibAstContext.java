package org.contract_lib.lang.contract_lib.contexts;

import org.contract_lib.contract_chameleon.SharedContextManager.SharedContext;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;

public class ContractLibAstContext implements SharedContext {

  private final ContractLibAst ast;

  public ContractLibAstContext(
      ContractLibAst ast) {
    this.ast = ast;
  }

  public ContractLibAst getAst() {
    return this.ast;
  }
}
