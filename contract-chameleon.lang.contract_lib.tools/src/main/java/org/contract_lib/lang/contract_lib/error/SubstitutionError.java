
package org.contract_lib.lang.contract_lib.error;

import org.contract_lib.contract_chameleon.error.ChameleonError;

public final class SubstitutionError implements ChameleonError {

  public String file;
  public int line;
  public int charIndex;
  public String message;

  public SubstitutionError(
      String file,
      int line,
      int charIndex,
      String message) {
    this.file = file;
    this.line = line;
    this.charIndex = charIndex;
    this.message = message;
  }

  public String getLocationIdentifier() {
    return file;
  }

  public int getLine() {
    return this.line;
  }

  public int getCharIndex() {
    return this.charIndex;
  }

  public String getMessage() {
    return this.message;
  }
}
