package org.contract_lib.lang.contract_lib;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.contract_lib.contract_chameleon.SharedContextManager;
import org.contract_lib.contract_chameleon.contexts.MessageContext;
import org.contract_lib.contract_chameleon.contexts.MessageContext.StringLogger;
import org.contract_lib.contract_chameleon.error.ChameleonMessageManager;
import org.contract_lib.contract_chameleon.error.ChameleonReportable;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.context_provider.AppliedAstExtensionsContextProvider;
import org.contract_lib.lang.contract_lib.context_provider.ContractLibAstContextProvider;
import org.contract_lib.lang.contract_lib.context_provider.FileIdentifierContextProvider;
import org.contract_lib.lang.contract_lib.context_provider.UndefinedCharStreamContextProvider;
import org.contract_lib.lang.contract_lib.context_provider.ast_extensions.AccessSortIdentifierContextProvider;
import org.contract_lib.lang.contract_lib.context_provider.ast_extensions.DefinedSortIdentifierContextProvider;
import org.contract_lib.lang.contract_lib.context_provider.ast_extensions.CommandOrderContextProvider;
import org.contract_lib.lang.contract_lib.context_provider.ast_extensions.FilePositionLinkerContextProvider;
import org.contract_lib.lang.contract_lib.context_provider.ast_extensions.ParentLinkerContextProvider;
import org.contract_lib.lang.contract_lib.contexts.AppliedAstExtensionsContext;
import org.contract_lib.lang.contract_lib.contexts.CharStreamContext;
import org.contract_lib.lang.contract_lib.contexts.ContractLibAstContext;
import org.contract_lib.lang.contract_lib.contexts.CurrentFileIdentifierContext;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.AccessSortIdentifierContext;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.DefinedSortIdentifierContext;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.CommandOrderContext;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.FilePositionLinkerContext;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.ParentLinkerContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class ContractLibAstTest {

  protected MessageContext messageContext;
  protected SharedContextManager sharedContextManager;
  protected ChameleonMessageManager messageManager;

  protected CurrentFileIdentifierContext fileIdentifierContext;
  protected CharStreamContext charStreamContext;

  protected AppliedAstExtensionsContext appliedAstExtensionsContext;

  protected FilePositionLinkerContext filePositionExtractor;
  protected ParentLinkerContext parentLinkerContext;
  protected CommandOrderContext commandOrderExtractor;
  protected DefinedSortIdentifierContext availableSortIdentifierContext;
  protected AccessSortIdentifierContext accessSortIdentifierContext;

  @BeforeEach
  protected void setupContext() {
    messageContext = new MessageContext(new StringLogger() {
      @Override
      public void log(String string) {
        System.err.println(string);
      }
    });

    sharedContextManager = new SharedContextManager(messageContext);
    messageManager = sharedContextManager.getMessageContext().getMessageManager();

    charStreamContext = sharedContextManager
        .getContext(new UndefinedCharStreamContextProvider())
        .get();
    fileIdentifierContext = sharedContextManager
        .getContext(new FileIdentifierContextProvider())
        .get();

    appliedAstExtensionsContext = sharedContextManager
        .getContext(new AppliedAstExtensionsContextProvider())
        .get();

    filePositionExtractor = sharedContextManager
        .getContext(new FilePositionLinkerContextProvider())
        .get();
    parentLinkerContext = sharedContextManager
        .getContext(new ParentLinkerContextProvider())
        .get();
    commandOrderExtractor = sharedContextManager
        .getContext(new CommandOrderContextProvider()).get();
    availableSortIdentifierContext = sharedContextManager
        .getContext(new DefinedSortIdentifierContextProvider())
        .get();
    accessSortIdentifierContext = sharedContextManager
        .getContext(new AccessSortIdentifierContextProvider())
        .get();

    appliedAstExtensionsContext.addAstExtensions(List.of(
        new FilePositionLinkerContextProvider(),
        new CommandOrderContextProvider(),
        new ParentLinkerContextProvider(),
        new DefinedSortIdentifierContextProvider(),
        new AccessSortIdentifierContextProvider()));
  }

  @AfterEach
  protected void printMessages() {
    messageContext.log();
  }

  public ContractLibAst createAstFromResourcePath(String path) throws IOException {
    try (InputStream in = getClass().getClassLoader().getResourceAsStream(path)) {

      assertNotNull(in, "Input stream not created from resource.");

      CharStream charStream = CharStreams.fromStream(in);
      charStreamContext.setCharStream(charStream);

      fileIdentifierContext.setFileIdentifier(String.format("<test ressources>/%s", path));

      ContractLibAstContext context = sharedContextManager.getContext(new ContractLibAstContextProvider()).get();

      return context.getAst();
    } catch (IOException exception) {
      throw exception;
    }
  }

  /// Test for errors that where not expected.
  protected void testUnexpectedErrors(Set<? extends ChameleonReportable> expectedErrors) {
    assertEquals(
        0,
        messageManager
            .getFlattendedMessages()
            .filter(Predicate.not(expectedErrors::contains))
            .count(),
        "There are unexpected errors.");
  }

  /// Test that all expected errors where logged
  protected void testExpectedErrors(Set<? extends ChameleonReportable> expectedErrors) {
    expectedErrors.forEach(this::testErrorLogged);
  }

  private void testErrorLogged(ChameleonReportable error) {
    assertEquals(1,
        messageManager
            .getFlattendedMessages()
            .filter((m) -> m.equals(error))
            .count(),
        String.format("The IdentifierError '%s' was not logged.", error.getMessage()));
  }
}
