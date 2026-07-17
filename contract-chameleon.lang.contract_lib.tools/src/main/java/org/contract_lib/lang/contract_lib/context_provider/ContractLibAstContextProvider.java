package org.contract_lib.lang.contract_lib.context_provider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

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

  private final List<? extends AstExtensionContextProvider<?>> astExtensionProviders;

  public ContractLibAstContextProvider(String filePath) throws IOException {
    this(filePath, CharStreams.fromFileName(filePath), List.of());
  }

  public ContractLibAstContextProvider(String filePath,
      List<AstExtensionContextProvider<?>> astExtensions)
      throws IOException {
    this(filePath, CharStreams.fromFileName(filePath), astExtensions);
  }

  public ContractLibAstContextProvider(Path filePath,
      List<AstExtensionContextProvider<?>> astExtensions)
      throws IOException {
    this(filePath.toString(), CharStreams.fromPath(filePath), astExtensions);
  }

  public ContractLibAstContextProvider(Path filePath) throws IOException {
    this(filePath.toString(), CharStreams.fromPath(filePath), List.of());
  }

  public ContractLibAstContextProvider(
      String source,
      CharStream stream,
      List<AstExtensionContextProvider<?>> astExtensions) {
    this.sourcePath = source;
    this.charStream = stream;
    this.astExtensionProviders = astExtensions;
  }

  @Override
  public ContractLibAstContext createContext(SharedContextManager sharedContextManager) {

    List<ContractLibAstTranslatorExtension> extensions = astExtensionProviders
        .stream()
        .flatMap(p -> this.getTranslatorExtensions(sharedContextManager, p))
        .toList();

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

    return new ContractLibAstContext(ast);
  }

  @Override
  public Class<ContractLibAstContext> getContext() {
    return ContractLibAstContext.class;
  }

  private <C extends AstExtensionContext> Stream<ContractLibAstTranslatorExtension> getTranslatorExtensions(
      SharedContextManager sharedContextManager,
      SharedContextProvider<C> contextProvider) {
    return sharedContextManager
        .getContext(contextProvider)
        .get()
        .getTranslatorExtension()
        .stream();
  }
}
