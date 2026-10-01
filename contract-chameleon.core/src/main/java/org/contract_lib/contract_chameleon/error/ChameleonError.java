
package org.contract_lib.contract_chameleon.error;

public interface ChameleonError extends ChameleonFileReportable {

  @Override
  public default String messageType() {
    return "ERROR";
  }
}
