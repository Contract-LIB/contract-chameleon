package org.contract_lib.contract_chameleon.error;

import java.util.List;
import java.util.stream.Collectors;

public final class ChameleonMessageGroup extends Exception {

  List<ChameleonReportable> messages;

  ChameleonMessageGroup(List<ChameleonReportable> messages) {
    this.messages = messages;
  }

  public String getMessage() {
    return messages.stream()
        .map(ChameleonReportable::getMessage)
        .collect(Collectors.joining(System.lineSeparator()));
  }

  public List<ChameleonReportable> getMessages() {
    return messages;
  }

}
