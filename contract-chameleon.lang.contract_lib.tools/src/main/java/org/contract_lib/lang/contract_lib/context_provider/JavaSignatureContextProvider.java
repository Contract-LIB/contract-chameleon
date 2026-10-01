package org.contract_lib.lang.contract_lib.context_provider;

import org.contract_lib.contract_chameleon.SharedContextManager;
import org.contract_lib.contract_chameleon.SharedContextManager.SharedContextProvider;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.contexts.JavaSignatureContext;

//NOTE:Only supports one AST at a time at the moment 
public class JavaSignatureContextProvider
    implements SharedContextProvider<JavaSignatureContext> {

  private final ContractLibAst ast;

  public JavaSignatureContextProvider(ContractLibAst ast) {
    this.ast = ast;
  }

  @Override
  public JavaSignatureContext createContext(SharedContextManager sharedContextManager) {
    return new JavaSignatureContext(ast, sharedContextManager.getMessageContext());
  }

  @Override
  public Class<JavaSignatureContext> getContext() {
    return JavaSignatureContext.class;
  }

}
