package org.contract_lib.lang.contract_lib.tester;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Set;
import java.util.stream.Stream;

import org.contract_lib.lang.contract_lib.ContractLibAstTest;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.error.IdentifierError;
import org.contract_lib.lang.contract_lib.error.IdentifierError.IdentifierErrorKind;
import org.contract_lib.lang.contract_lib.label.IdentifierType.SortIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class TestSortTests extends ContractLibAstTest {

  static final String REDEFINED_VALUE = "Dup";
  static final String UNDEFINED_VALUE = "UnDef";
  static final String FILE_IDENTIFIER_REDEF = "<test ressources>/tester/sort/RedefinedSorts.clib";
  static final String FILE_IDENTIFIER_UNDEF = "<test ressources>/tester/sort/UndefinedSortAccess.clib";

  private static IdentifierError getUndefError(int line, int charIndex) {
    return new IdentifierError(
        IdentifierErrorKind.UNDEFINED,
        UNDEFINED_VALUE,
        new SortIdentifier(),
        FILE_IDENTIFIER_UNDEF,
        line, charIndex);
  }

  private static IdentifierError getRedefError(int line, int charIndex) {
    return new IdentifierError(
        IdentifierErrorKind.REDEFINED,
        REDEFINED_VALUE,
        new SortIdentifier(),
        FILE_IDENTIFIER_REDEF,
        line, charIndex);
  }

  static Stream<Arguments> sorts() {
    return Stream.of(
        Arguments.of("tester/sort/DefinedSortAccess.clib", Set.of()),
        Arguments.of("tester/sort/RedefinedSorts.clib", Set.of(
            getRedefError(3, 1),
            getRedefError(8, 1),
            getRedefError(16, 4),
            getRedefError(17, 4),
            getRedefError(29, 1),
            getRedefError(37, 4),
            getRedefError(38, 4))),
        Arguments.of("tester/sort/UndefinedSortAccess.clib", Set.of(
            getUndefError(9, 12),
            getUndefError(9, 22),
            getUndefError(20, 12),
            getUndefError(20, 22),
            getUndefError(23, 12),
            getUndefError(23, 22),
            getUndefError(30, 10),
            getUndefError(30, 20),
            getUndefError(41, 12),
            getUndefError(41, 22),
            getUndefError(44, 12),
            getUndefError(44, 22),
            getUndefError(52, 4),
            getUndefError(53, 4))));
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

    tester.testSorts(
        fileIdentifierContext,
        filePositionExtractor,
        definedSortIdentifierContext,
        accessSortIdentifierContext,
        availableSortIdentifierContext);

    System.err.println(availableSortIdentifierContext);
    testUnexpectedErrors(expectedErrors);
    testExpectedErrors(expectedErrors);
  }
}
