package org.contract_lib.lang.contract_lib.generator;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.contract_lib.contract_chameleon.SharedContextManager;
import org.contract_lib.contract_chameleon.contexts.MessageContext;
import org.contract_lib.contract_chameleon.contexts.MessageContext.StringLogger;
import org.contract_lib.contract_chameleon.error.ChameleonMessageManager;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.context_provider.ContractLibAstContextProvider;
import org.contract_lib.lang.contract_lib.context_provider.ast_extensions.CommandOrderContextProvider;
import org.contract_lib.lang.contract_lib.context_provider.ast_extensions.FilePositionLinkerContextProvider;
import org.contract_lib.lang.contract_lib.contexts.ContractLibAstContext;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.CommandOrderContext;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.FilePositionLinkerContext;
import org.junit.jupiter.api.BeforeEach;

public class ContractLibAstTest {

  protected SharedContextManager sharedContextManager = null;
  protected MessageContext messageContext = null;
  protected ChameleonMessageManager messageManager = null;
  protected FilePositionLinkerContext filePositionExtractor = null;
  protected CommandOrderContext commandOrderExtractor = null;

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
    filePositionExtractor = sharedContextManager.getContext(new FilePositionLinkerContextProvider()).get();
    commandOrderExtractor = sharedContextManager.getContext(new CommandOrderContextProvider()).get();
  }

  public ContractLibAst createAstFromResourcePath(String path) throws IOException {
    try (InputStream in = getClass().getClassLoader().getResourceAsStream(path)) {

      assertNotNull(in, "Input stream not created from resource.");

      CharStream charStream = CharStreams.fromStream(in);

      ContractLibAstContextProvider provider = new ContractLibAstContextProvider(
          path,
          charStream, List.of(
              new FilePositionLinkerContextProvider(),
              new CommandOrderContextProvider()));

      ContractLibAstContext context = sharedContextManager.getContext(provider).get();

      return context.getAst();
    } catch (IOException exception) {
      throw exception;
    }
  }
}
