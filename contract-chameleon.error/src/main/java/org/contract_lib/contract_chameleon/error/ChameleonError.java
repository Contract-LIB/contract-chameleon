package org.contract_lib.contract_chameleon.error;

//TODO: Change to FileReportable
public abstract class ChameleonError implements ChameleonReportable {

  @Override
  public final String messageType() {
    return "ERROR";
  }
}
