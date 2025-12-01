
package org.contract_lib.lang.contract_lib.generator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import java.util.stream.Stream;

import java.io.InputStream;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

import org.contract_lib.contract_chameleon.error.ChameleonMessageManager;
import org.contract_lib.contract_chameleon.error.ChameleonMessageGroup;

import org.contract_lib.lang.contract_lib.ast.ContractLibAst;

class CreateAstFromSourceErrorTest {

  static Stream<Arguments> definitionErrors() {
    return Stream.of(
        Arguments.of("ast/errors/MissingCommand.clib", List.of()),
        Arguments.of("ast/errors/TyposKeywordSort.clib", List.of()),
        Arguments.of("ast/errors/TyposKeywordContract.clib", List.of()),
        Arguments.of("ast/errors/abstractions/TyposKeyword.clib", List.of()),
        Arguments.of("ast/errors/abstractions/MissingBracket.clib", List.of()));
  }

  @ParameterizedTest
  @MethodSource("definitionErrors")
  void testInvalidDeclarations(
      String filePath,
      List<Arguments> errors) throws Exception {

    InputStream in = getClass().getClassLoader().getResourceAsStream(filePath);
    assertNotNull(in, "Input stream not created from resource.");

    CharStream charStream = CharStreams.fromStream(in);
    ChameleonMessageManager messageManager = new ChameleonMessageManager();

    ContractLibGenerator generator = new ContractLibGenerator(messageManager);
    ContractLibAst ast = generator.generate(filePath, charStream);

    messageManager.writeStdErr();

    System.err.println(filePath);

    assertThrows(
        ChameleonMessageGroup.class,
        messageManager::check,
        "An error is expected.");

    //TODO: Check number of exceptions
    //TODO: Report error when bracket is missing
    //TODO: Better locations of syntax errors / not the occurence of next Token, but where the construct is incompleat
  }
}
