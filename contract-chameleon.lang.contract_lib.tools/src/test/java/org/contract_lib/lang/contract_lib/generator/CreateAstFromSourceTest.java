package org.contract_lib.lang.contract_lib.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.contract_lib.lang.contract_lib.ContractLibAstTest;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class CreateAstFromSourceTest extends ContractLibAstTest {

  static Stream<Arguments> positiveDefinitions() {
    return Stream.of(
        Arguments.of("ast/DeclareAbstraction.clib", 3, 0, 0),
        Arguments.of("ast/DeclareDatatype.clib", 0, 3, 0),
        Arguments.of("ast/DeclareSort.clib", 0, 0, 4));
  }

  static Stream<Arguments> definitionErrors() {
    return Stream.of(
        Arguments.of("ast/InvalidTopLevel.clib", 2, 2, 3));
  }

  @ParameterizedTest
  @MethodSource("positiveDefinitions")
  void testDeclarations(
      String filePath,
      int nAbstractions,
      int nDatatypes,
      int nSorts) throws Exception {
    ContractLibAst ast = this.createAstFromResourcePath(filePath);
    assertFalse(messageManager::errorFound, "There was a message generated.");
    assertNotNull(ast, "AST could not be created.");

    assertEquals(nAbstractions, ast.abstractions().size(), "Wrong numner of abstractions found.");
    assertEquals(nDatatypes, ast.datatypes().size(), "Wrong numner of abstractions found.");
    assertEquals(nSorts, ast.sorts().size(), "Wrong numner of sorts found.");
  }

  @ParameterizedTest
  @MethodSource("definitionErrors")
  void testInvalidDeclarations(
      String filePath,
      int nAbstractions,
      int nDatatypes,
      int nSorts) throws Exception {
    ContractLibAst ast = this.createAstFromResourcePath(filePath);
    assertTrue(
        messageManager::errorFound,
        "An error is expected.");

    //TODO: Check number of exceptions
    //TODO: Report error when bracket is missing
    //TODO: Better locations of syntax errors / not the occurence of next Token, but where the construct is incompleat

    assertNotNull(ast, "AST could not be created.");
    assertEquals(nAbstractions, ast.abstractions().size(), "Wrong numner of abstractions found.");
    assertEquals(nDatatypes, ast.datatypes().size(), "Wrong numner of abstractions found.");
    assertEquals(nSorts, ast.sorts().size(), "Wrong numner of sorts found.");
  }
}
