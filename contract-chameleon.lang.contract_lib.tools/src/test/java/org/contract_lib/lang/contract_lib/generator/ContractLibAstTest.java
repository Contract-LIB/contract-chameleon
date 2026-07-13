package org.contract_lib.lang.contract_lib.generator;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.contract_lib.contract_chameleon.SharedContextManager;
import org.contract_lib.contract_chameleon.contexts.MessageContext;
import org.contract_lib.contract_chameleon.contexts.MessageContext.StringLogger;
import org.contract_lib.contract_chameleon.error.ChameleonMessageManager;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.context_provider.AstExtensionContextProvider;
import org.contract_lib.lang.contract_lib.context_provider.ContractLibAstContextProvider;
import org.contract_lib.lang.contract_lib.contexts.AstExtensionContext;
import org.contract_lib.lang.contract_lib.contexts.ContractLibAstContext;
import org.contract_lib.lang.contract_lib.translator_extensions.CommandOrderExtractor;
import org.contract_lib.lang.contract_lib.translator_extensions.FilePositionLinker;
import org.junit.jupiter.api.BeforeEach;

public class ContractLibAstTest {

  protected MessageContext messageContext = null;
  protected SharedContextManager sharedContextManager = null;
  protected ChameleonMessageManager messageManager = null;
  protected FilePositionLinker filePositionExtractor = null;
  protected CommandOrderExtractor commandOrderExtractor = null;

  @BeforeEach
  void setupContext() {
    messageContext = new MessageContext(new StringLogger() {
      @Override
      public void log(String string) {
        System.err.println(string);
      }
    });
    sharedContextManager = new SharedContextManager(messageContext);
    messageManager = sharedContextManager.getMessageContext().getMessageManager();

    AstExtensionContext extensionContext = sharedContextManager
        .getContext(new AstExtensionContextProvider())
        .get();
    filePositionExtractor = extensionContext.getFilePositionLinker();
    commandOrderExtractor = extensionContext.getCommandOrderExtractor();
  }

  public ContractLibAst createAstFromResourcePath(String path) throws IOException {
    try (InputStream in = getClass().getClassLoader().getResourceAsStream(path)) {

      assertNotNull(in, "Input stream not created from resource.");

      CharStream charStream = CharStreams.fromStream(in);

      ContractLibAstContextProvider provider = new ContractLibAstContextProvider(
          path,
          charStream);

      ContractLibAstContext context = provider
          .createContext(sharedContextManager);

      return context.getAst();
    } catch (IOException exception) {
      throw exception;
    }
  }
}
