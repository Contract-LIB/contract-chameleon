
package org.contract_lib.contract_chameleon.adapters;

import java.nio.file.Path;
import java.util.Optional;

import org.contract_lib.contract_chameleon.contexts.ResultDirectoryContext;
import org.contract_lib.contract_chameleon.contexts.SourcePathsContext;
import org.contract_lib.contract_chameleon.contexts.ResultDirectoryContext.Dir;

public abstract class ExportAdapter extends TranslationAdapter {

  private ResultDirectoryContext result;

  @Override
  public final void performTranslation() {

    Optional<SourcePathsContext> sourcesContext = getContext(SourcePathsContext.class);
    Optional<ResultDirectoryContext> resultContext = getContext(ResultDirectoryContext.class);
    if (sourcesContext.isEmpty()) {
      getMessageContext().logError("Interface: ExportAdapters requires SourcePathsContext.");
      return;
    }
    if (resultContext.isEmpty()) {
      getMessageContext().logError("Interface: ExportAdapters requires ResultDirectoryContext required.");
      return;
    }
    this.result = resultContext.get();

    if (sourcesContext.get().getPaths().size() == 1) {
      this.performForPath(this.result.getResultDirectory());
    } else {
      getMessageContext().logError("Only on Contract-LIB file is supported at a time.");
    }
  }

  final void performAddingSubdir(Path p) {
    String filename = p.getFileName().toString();
    Dir finalDir = result.getResultDirectory().addSubDirectories(filename);
    performForPath(finalDir);
  }

  /** Given a path to a single ContractLIB file, creates a translation in the result directory.
   *
   * @param p the path to the ContractLIB file.
   * @param finalDir the directory where the results should be written.
   */
  public abstract void performForPath(Dir finalDir);

}
