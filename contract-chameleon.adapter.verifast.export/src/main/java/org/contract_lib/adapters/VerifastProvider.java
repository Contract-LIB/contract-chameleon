package org.contract_lib.adapters;

import java.io.IOException;
import java.util.List;
import java.nio.file.Path;

import org.contract_lib.contract_chameleon.ExportAdapter;
import org.contract_lib.contract_chameleon.error.ChameleonMessageManager;
import org.contract_lib.contract_chameleon.error.ChameleonMessageType;
import org.contract_lib.contract_chameleon.error.SimpleErrorMessage;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.generator.ContractLibGenerator;

public final class VerifastProvider extends ExportAdapter {

  public String defaultOutputDir() {
    return "verifast-provider";
  }

  public String adapterTitle() {
    return "Verifast Provider Adapter";
  }

  public String getAdapterName() {
    return "verifast-provider";
  }

  public List<TranslationResult> perform(
      List<Path> sourceFiles,
      ChameleonMessageManager messageManager) throws IOException {

    //TODO: Support mulitple files

    messageManager.report(new SimpleErrorMessage("This provider supports only one source file at the moment.",
        ChameleonMessageType.INFO, null));
    Path fileName = sourceFiles.get(0);

    ContractLibGenerator generator = new ContractLibGenerator(messageManager);

    ContractLibAst ast = generator.generateFromPath(fileName);
    SimpleVerifastTranslator trans = new SimpleVerifastTranslator(fileName, messageManager);
    List<TranslationResult> results = trans.translateContractLibAstProvider(ast);

    return results;
  }
}
