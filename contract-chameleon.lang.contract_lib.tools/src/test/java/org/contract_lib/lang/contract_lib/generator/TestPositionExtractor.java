package org.contract_lib.lang.contract_lib.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.contract_lib.contract_chameleon.error.ChameleonMessageManager;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;
import org.contract_lib.lang.contract_lib.translator_extensions.FilePositionExtractor;
import org.junit.jupiter.api.Test;

class TestPositionExtractor {

  // Ensure that two different AST records with the same values
  // still have different labels.
  @Test
  void testIdenticalDef() throws Exception {

    String filePath = "extractors/IdenticalDef.clib";
    InputStream in = getClass().getClassLoader().getResourceAsStream(filePath);
    assertNotNull(in, "Input stream not created from resource.");

    CharStream charStream = CharStreams.fromStream(in);
    ChameleonMessageManager messageManager = new ChameleonMessageManager();
    FilePositionExtractor filePos = new FilePositionExtractor();

    ContractLibGenerator generator = new ContractLibGenerator(messageManager, List.of(filePos));
    ContractLibAst ast = generator.generate(filePath, charStream);

    assertFalse(messageManager::errorFound, "There was an message generated.");
    assertNotNull(ast, "AST could not be created.");

    ContractLibAstElement pre = ast.contracts().getFirst().pairs().getFirst().pre();
    ContractLibAstElement post = ast.contracts().getFirst().pairs().getFirst().post();

    assertEquals(pre, post, "The values of the compared records must be equal.");
    assertNotEquals(
        filePos.getFilePosition(pre),
        filePos.getFilePosition(post),
        "The fetched file positions must differ.");
  }
}
