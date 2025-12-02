
package org.contract_lib.contract_chameleon.error;

//TODO: Change to FileReportable
public abstract class ChameleonWarning implements ChameleonReportable {
  @Override
  public final String messageType() {
    return "WARNING";
  }
}
