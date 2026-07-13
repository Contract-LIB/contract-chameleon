package org.contract_lib.lang.contract_lib.contexts.ast_extensions;

import org.contract_lib.lang.contract_lib.contexts.AstExtensionContext;
import org.contract_lib.lang.contract_lib.translator_extensions.FilePositionLinker;

/// An context linking the AST elements with their position in the source file.
public class FilePositionLinkerContext implements AstExtensionContext<FilePositionLinker> {

  private final FilePositionLinker filePositionLinker;

  public FilePositionLinkerContext() {
    this.filePositionLinker = new FilePositionLinker();
  }

  public FilePositionLinker getTranslatorExtension() {
    return this.filePositionLinker;
  }

  //private final AccSortIdentifierExtractor accSortIdentifierExtractor;
  //private final AccVariableIdentifierExtractor accVariableIdentifierExtractor;
}
