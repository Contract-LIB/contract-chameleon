package org.contract_lib.lang.contract_lib.context_provider;

import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.contract_lib.contract_chameleon.SharedContextManager;
import org.contract_lib.contract_chameleon.SharedContextManager.SharedContextProvider;
import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBLexer;
import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.contexts.AppliedAstExtensionsContext;
import org.contract_lib.lang.contract_lib.contexts.AstExtensionContext;
import org.contract_lib.lang.contract_lib.contexts.CharStreamContext;
import org.contract_lib.lang.contract_lib.contexts.ContractLibAstContext;
import org.contract_lib.lang.contract_lib.contexts.CurrentFileIdentifierContext;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstErrorListener;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstGenerator;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstTranslatorExtension;

//NOTE:Only supports one AST at a time at the moment 
public class ContractLibAstContextProvider
    implements SharedContextProvider<ContractLibAstContext> {

  @Override
  public ContractLibAstContext createContext(SharedContextManager sharedContextManager) {

    AppliedAstExtensionsContext extensionsContext = sharedContextManager
        .getContext(new AppliedAstExtensionsContextProvider())
        .get();

    CharStreamContext charStream = sharedContextManager
        .getContext(new CharStreamContextProvider())
        .get();

    CurrentFileIdentifierContext sourcePath = sharedContextManager
        .getContext(new FileIdentifierContextProvider())
        .get();

    List<ContractLibAstTranslatorExtension> extensions = extensionsContext.getExtensionProviderContexts()
        .stream()
        .flatMap(AstExtensionContext::getTranslatorExtensionStream)
        .toList();

    ContractLIBLexer lexer = new ContractLIBLexer(charStream.getCharStream().get());
    CommonTokenStream tokenStream = new CommonTokenStream(lexer);
    ContractLIBParser parser = new ContractLIBParser(tokenStream);

    ContractLibAstErrorListener errorHandler = new ContractLibAstErrorListener(
        sourcePath.getFileIdentifier(),
        sharedContextManager.getMessageContext().getMessageManager());

    parser.addErrorListener(errorHandler);

    ContractLIBParser.Start_Context parseTree = parser.start_();

    ContractLibAstGenerator generate = new ContractLibAstGenerator(
        extensions);

    ContractLibAst ast = generate.translateStart(parseTree);

    return new ContractLibAstContext(ast);
  }

  @Override
  public Class<ContractLibAstContext> getContext() {
    return ContractLibAstContext.class;
  }

}
