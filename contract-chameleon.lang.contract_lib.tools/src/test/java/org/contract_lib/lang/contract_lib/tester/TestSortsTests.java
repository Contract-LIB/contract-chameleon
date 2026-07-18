package org.contract_lib.lang.contract_lib.tester;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Set;
import java.util.stream.Stream;

import org.contract_lib.lang.contract_lib.ContractLibAstTest;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.error.IdentifierError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class TestSortTests extends ContractLibAstTest {

  static Stream<Arguments> sorts() {
    return Stream.of(
        Arguments.of("tester/sort/RedefinedSorts.clib", Set.of()),
        Arguments.of("tester/sort/UndefinedSortAccess.clib", Set.of()));
  }

  TestSorts tester;

  @BeforeEach
  @Override
  protected void setupContext() {
    super.setupContext();
    tester = new TestSorts(messageContext);
  }

  @ParameterizedTest
  @MethodSource("sorts")
  void testDeclarations(
      String filePath,
      Set<IdentifierError> expectedErrors) throws Exception {

    ContractLibAst ast = this.createAstFromResourcePath(filePath);
    assertFalse(messageManager::errorFound, "There was a message generated.");

    System.err.println(ast);
    System.err.println(
        availableSortIdentifierContext.getExtractor().allIdentifier());
    System.err.println(
        accessSortIdentifierContext.getNodesAccessingSortIdentifier());

    tester.testSorts(
        fileIdentifierContext,
        filePositionExtractor,
        availableSortIdentifierContext,
        accessSortIdentifierContext);

    testUnexpectedErrors(expectedErrors);
    testExpectedErrors(expectedErrors);
  }
}
