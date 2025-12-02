package org.contract_lib.contract_chameleon.error;

public abstract class ChameleonFileReportable implements ChameleonReportable {
  abstract String getLocationIdentifier();

  abstract int getLine();

  abstract int getCharIndex();

  public final String getMessage() {
    return String.format(
        "%s in %s: %d|%d -> %s",
        this.messageType(),
        this.getLocationIdentifier(),
        this.getLine(),
        this.getCharIndex(),
        this.getMessage());
  }

  public abstract String messageType();
}
