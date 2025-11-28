package org.contract_lib.lang.contract_lib.generator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.stream.Stream;

import java.io.InputStream;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

import org.contract_lib.contract_chameleon.error.ChameleonMessageManager;
import org.contract_lib.contract_chameleon.error.ChameleonMessageGroup;

import org.contract_lib.lang.contract_lib.ast.ContractLibAst;

class CreateAstFromSourceTest {

  static Stream<Arguments> positiveDefinitions() {
    return Stream.of(
        Arguments.of("ast/DeclareAbstraction.clib", 3, 0, 0),
        Arguments.of("ast/DeclareDatatype.clib", 0, 3, 0),
        Arguments.of("ast/DeclareSort.clib", 0, 0, 4));
  }

  @ParameterizedTest
  @MethodSource("positiveDefinitions")
  void testDeclarations(
      String filePath,
      int nAbstractions,
      int nDatatypes,
      int nSorts) throws Exception {
    InputStream in = getClass().getClassLoader().getResourceAsStream(filePath);
    assertNotNull(in, "Input stream not created from resource.");

    CharStream charStream = CharStreams.fromStream(in);
    ChameleonMessageManager messageManager = new ChameleonMessageManager();

    ContractLibGenerator generator = new ContractLibGenerator(messageManager);
    ContractLibAst ast = generator.generate(filePath, charStream);
    System.err.println(ast);

    assertDoesNotThrow(messageManager::check, "There was an message generated.");
    assertNotNull(ast, "AST could not be created.");

    assertEquals(nAbstractions, ast.abstractions().size(), "Wrong numner of abstractions found.");
    assertEquals(nDatatypes, ast.datatypes().size(), "Wrong numner of abstractions found.");
    assertEquals(nSorts, ast.sorts().size(), "Wrong numner of sorts found.");
  }

  static Stream<Arguments> termTests() {
    return Stream.of(
        Arguments.of("ast/terms/BooleanTests.clib"));
  }

  @ParameterizedTest
  @MethodSource("termTests")
  void testInvalidDeclarations(
      String filePath
      ) throws Exception {
    InputStream in = getClass().getClassLoader().getResourceAsStream(filePath);
    assertNotNull(in, "Input stream not created from resource.");

    CharStream charStream = CharStreams.fromStream(in);
    ChameleonMessageManager messageManager = new ChameleonMessageManager();

    ContractLibGenerator generator = new ContractLibGenerator(messageManager);
    ContractLibAst ast = generator.generate(filePath, charStream);

    System.err.println(ast);
    messageManager.writeStdErr();
    assertDoesNotThrow(messageManager::check, "There was an message generated.");
  }
}
