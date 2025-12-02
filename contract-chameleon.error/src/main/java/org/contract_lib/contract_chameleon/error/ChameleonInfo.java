package org.contract_lib.contract_chameleon.error;

//TODO: Change to FileReportable
public abstract class ChameleonInfo implements ChameleonReportable {
  @Override
  public final String messageType() {
    return "INFO";
  }
}
