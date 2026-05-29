package org.contract_lib.lang.contract_lib.contexts;

import java.util.List;

import org.contract_lib.contract_chameleon.SharedContextManager.SharedContext;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstTranslatorExtension;
import org.contract_lib.lang.contract_lib.translator_extensions.AccSortIdentifierExtractor;
import org.contract_lib.lang.contract_lib.translator_extensions.AccVariableIdentifierExtractor;
import org.contract_lib.lang.contract_lib.translator_extensions.CommandOrderExtractor;
import org.contract_lib.lang.contract_lib.translator_extensions.FilePositionLinker;

// TODO: Split up in different contexts
public class AstExtensionContext implements SharedContext {

  private final CommandOrderExtractor commandOrderExtractor;
  private final FilePositionLinker filePositionLinker;
  private final AccSortIdentifierExtractor accSortIdentifierExtractor;
  private final AccVariableIdentifierExtractor accVariableIdentifierExtractor;

  public AstExtensionContext() {
    this.commandOrderExtractor = new CommandOrderExtractor();
    this.filePositionLinker = new FilePositionLinker();
    this.accSortIdentifierExtractor = new AccSortIdentifierExtractor();
    this.accVariableIdentifierExtractor = new AccVariableIdentifierExtractor();
  }

  public List<ContractLibAstTranslatorExtension> getAllExtensions() {
    return List.of(
        this.commandOrderExtractor,
        this.filePositionLinker,
        this.accSortIdentifierExtractor,
        this.accVariableIdentifierExtractor);
  }

  public CommandOrderExtractor getCommandOrderExtractor() {
    return commandOrderExtractor;
  }

  public FilePositionLinker getFilePositionLinker() {
    return filePositionLinker;
  }

  public AccSortIdentifierExtractor getAccSortIdentifierExtractor() {
    return accSortIdentifierExtractor;
  }

  public AccVariableIdentifierExtractor getAccVariableIdentifierExtractor() {
    return accVariableIdentifierExtractor;
  }
}
