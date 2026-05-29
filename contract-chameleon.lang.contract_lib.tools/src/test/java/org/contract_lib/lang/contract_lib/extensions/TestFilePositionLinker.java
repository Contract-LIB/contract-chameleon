package org.contract_lib.lang.contract_lib.extensions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstTest;
import org.junit.jupiter.api.Test;

class TestFilePositionLinker extends ContractLibAstTest {

  // Ensure that two different AST records with the same values
  // still have different labels.
  @Test
  void testIdenticalDef() throws Exception {

    String filePath = "extractors/IdenticalDef.clib";
    ContractLibAst ast = this.createAstFromResourcePath(filePath);

    assertFalse(messageManager::errorFound, "There was an error message generated.");
    assertNotNull(ast, "AST could not be created.");

    ContractLibAstElement pre = ast.contracts().getFirst().pairs().getFirst().pre();
    ContractLibAstElement post = ast.contracts().getFirst().pairs().getFirst().post();

    assertEquals(pre, post, "The values of the compared records must be equal.");
    assertNotEquals(
        filePositionExtractor.getFilePosition(pre),
        filePositionExtractor.getFilePosition(post),
        "The fetched file positions must differ.");
  }
}
