package org.contract_lib.adapters;

import java.io.IOException;

import java.util.List;

import java.nio.file.Path;

import org.contract_lib.contract_chameleon.ImportAdapter;
import org.contract_lib.contract_chameleon.error.ChameleonMessageManager;

public final class KeyImportApplicant extends ImportAdapter {

  public String defaultOutputDir() {
    return "key-import";
  }

  public String adapterTitle() {
    return "KeY Import Adapter";
  }

  public String getAdapterName() {
    return "key-import";
  }

  public List<TranslationResult> perform(
      List<Path> sourceFiles,
      ChameleonMessageManager messageManager) throws IOException {

    SimpleKeyImportTranslator translator = new SimpleKeyImportTranslator(messageManager);

    return translator.translate(sourceFiles);
  }
}
