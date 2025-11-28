package org.contract_lib.lang.contract_lib.error;

import java.util.Optional;

import org.contract_lib.contract_chameleon.error.ChameleonReportable;
import org.contract_lib.contract_chameleon.error.ChameleonMessageType;

/// Dimension errors, where lists are expected to have the same length but do not.
public final class DimensionError implements ChameleonReportable {

  private String file;
  private int line;
  private int charIndex;
  private String message;

  public DimensionError(
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

  public ChameleonMessageType messageType() {
    return ChameleonMessageType.ERROR;
  }

  public Optional<String> getDetailedMessage() {
    return Optional.empty();
  }
}
