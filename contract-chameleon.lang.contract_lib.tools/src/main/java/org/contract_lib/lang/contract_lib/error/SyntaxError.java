package org.contract_lib.lang.contract_lib.error;

import java.util.Optional;

import org.contract_lib.contract_chameleon.error.ChameleonMessageManager;
import org.contract_lib.contract_chameleon.error.ChameleonReportable;
import org.contract_lib.contract_chameleon.error.ChameleonMessageType;

public final class SyntaxError implements ChameleonReportable {

  public String file;
  public int line;
  public int charIndex;
  public String message;

  public SyntaxError(
    String file,
    int line,
    int charIndex,
    String message
  ) {
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
  public ChameleonMessageType messageType() {
    return ChameleonMessageType.ERROR; 
  }


  public Optional<String> getDetailedMessage() {
    return Optional.empty();
  }
}
