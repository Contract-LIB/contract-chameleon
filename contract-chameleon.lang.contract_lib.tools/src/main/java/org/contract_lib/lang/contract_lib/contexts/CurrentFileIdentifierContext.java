
package org.contract_lib.lang.contract_lib.contexts;

import java.nio.file.Path;

import org.contract_lib.contract_chameleon.SharedContextManager.SettableContext;

/// An context linking the AST elements with their position in the source file.
public class CurrentFileIdentifierContext implements SettableContext {

  private String fileIdentifier;
  private Path filePath;

  public CurrentFileIdentifierContext(String fileIdentifier) {
    this.fileIdentifier = fileIdentifier;
    this.filePath = Path.of(this.fileIdentifier);
  }

  public String getFileIdentifier() {
    return fileIdentifier;
  }

  public Path getFilePath() {
    return filePath;
  }

  public void setFileIdentifier(String fileIdentifier) {
    this.fileIdentifier = fileIdentifier;
    this.filePath = Path.of(this.fileIdentifier);
  }

  public void setFileIdentifier(Path fileIdentifier) {
    this.filePath = fileIdentifier;
    this.fileIdentifier = fileIdentifier.toString();
  }
}
