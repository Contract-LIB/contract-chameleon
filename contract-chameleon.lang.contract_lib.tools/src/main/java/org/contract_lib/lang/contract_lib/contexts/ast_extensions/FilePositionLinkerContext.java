package org.contract_lib.lang.contract_lib.contexts.ast_extensions;

import java.util.List;
import java.util.Optional;

import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;
import org.contract_lib.lang.contract_lib.contexts.AstExtensionContext;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstTranslatorExtension;
import org.contract_lib.lang.contract_lib.label.FilePosition;
import org.contract_lib.lang.contract_lib.translator_extensions.FilePositionLinker;

/// An context linking the AST elements with their position in the source file.
public class FilePositionLinkerContext implements AstExtensionContext {

  private final FilePositionLinker filePositionLinker;

  public FilePositionLinkerContext() {
    this.filePositionLinker = new FilePositionLinker();
  }

  public List<ContractLibAstTranslatorExtension> getTranslatorExtension() {
    return List.of(this.filePositionLinker);
  }

  public Optional<FilePosition> getFilePosition(ContractLibAstElement element) {
    return filePositionLinker.getFilePosition(element);
  }

  //private final AccSortIdentifierExtractor accSortIdentifierExtractor;
  //private final AccVariableIdentifierExtractor accVariableIdentifierExtractor;
}
