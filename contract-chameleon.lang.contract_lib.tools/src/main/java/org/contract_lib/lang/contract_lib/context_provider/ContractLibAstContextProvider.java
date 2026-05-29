package org.contract_lib.lang.contract_lib.context_provider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.contract_lib.contract_chameleon.SharedContextManager;
import org.contract_lib.contract_chameleon.SharedContextManager.SharedContextProvider;
import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBLexer;
import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.contexts.AstExtensionContext;
import org.contract_lib.lang.contract_lib.contexts.ContractLibAstContext;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstErrorListener;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstGenerator;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstTranslatorExtension;

//NOTE:Only supports one AST at a time at the moment 
public class ContractLibAstContextProvider
    implements SharedContextProvider<ContractLibAstContext> {

  private final String sourcePath;
  private final CharStream charStream;

  public ContractLibAstContextProvider(String filePath) throws IOException {
    this(filePath, CharStreams.fromFileName(filePath));
  }

  public ContractLibAstContextProvider(Path filePath) throws IOException {
    this(filePath.toString(), CharStreams.fromPath(filePath));
  }

  public ContractLibAstContextProvider(
      String source,
      CharStream stream) {
    this.sourcePath = source;
    this.charStream = stream;
  }

  @Override
  public ContractLibAstContext createContext(SharedContextManager sharedContextManager) {

    Optional<AstExtensionContext> extensionContext = sharedContextManager
        .getContext(new AstExtensionContextProvider());

    List<ContractLibAstTranslatorExtension> extensions = extensionContext.map(AstExtensionContext::getAllExtensions)
        .orElseGet(() -> List.of());

    ContractLIBLexer lexer = new ContractLIBLexer(charStream);
    CommonTokenStream tokenStream = new CommonTokenStream(lexer);
    ContractLIBParser parser = new ContractLIBParser(tokenStream);

    ContractLibAstErrorListener errorHandler = new ContractLibAstErrorListener(
        sourcePath,
        sharedContextManager.getMessageContext().getMessageManager());

    parser.addErrorListener(errorHandler);

    ContractLIBParser.Start_Context parseTree = parser.start_();

    ContractLibAstGenerator generate = new ContractLibAstGenerator(
        extensions);

    ContractLibAst ast = generate.translateStart(parseTree);

    return new ContractLibAstContext(
        ast);
  }

  @Override
  public Class<ContractLibAstContext> getContext() {
    return ContractLibAstContext.class;
  }
}
