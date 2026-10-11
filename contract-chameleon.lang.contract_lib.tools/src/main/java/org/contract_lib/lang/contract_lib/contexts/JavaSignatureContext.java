package org.contract_lib.lang.contract_lib.contexts;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.contract_lib.contract_chameleon.SharedContextManager.SharedContext;
import org.contract_lib.contract_chameleon.contexts.MessageContext;
import org.contract_lib.lang.contract_lib.ast.Abstraction;
import org.contract_lib.lang.contract_lib.ast.Contract;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.tools.ClassNameExtractor;
import org.contract_lib.lang.contract_lib.tools.JavaMethodSignaturExtractor;

public class JavaSignatureContext implements SharedContext {

  private final Map<Abstraction, ClassNameExtractor> className = new HashMap<>();
  private final Map<Contract, JavaMethodSignaturExtractor> methodName = new HashMap<>();

  public JavaSignatureContext(ContractLibAst ast, MessageContext context) {

    ast.abstractions().stream()
        .forEach((a) -> className.put(a, new ClassNameExtractor(a, context.getMessageManager())));

    ast.contracts().stream()
        .forEach((c) -> methodName.put(c, new JavaMethodSignaturExtractor(c, context.getMessageManager())));
  }

  public Optional<Void> getClassName(Abstraction abstraction) {
    //TODO
    return null;
  }

  public Optional<Void> getMethodOwner(Contract contract) {
    //TODO
    return null;
  }

  public Optional<Void> getMethodName(Contract contract) {
    //TODO
    return null;
  }

  public Optional<Void> getMethodSignature(Contract contract) {
    //TODO
    return null;
  }

  // Placeholder

  public Optional<Void> getPlaceholderMethodIdentifier(Contract contract) {
    //TODO
    return null;
  }

  public Optional<Void> getPlaceholderMethodComment(Contract contract) {
    //TODO
    return null;
  }
}
