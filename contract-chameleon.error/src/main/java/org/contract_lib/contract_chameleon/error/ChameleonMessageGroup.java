package org.contract_lib.contract_chameleon.error;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public final class ChameleonMessageGroup extends Exception {

  public final List<ChameleonReportable> messages;

  public ChameleonMessageGroup(List<ChameleonReportable> messages) {
    this.messages = new ArrayList(messages);
  }

  public String getMessage() {
    return this.messages.stream().sorted()
      .map(this::messageDescription)
      .collect(Collectors.joining(System.lineSeparator()));
  } 

  public List<ChameleonReportable> getMessages() {
    return new ArrayList(this.messages);
  }

  private String messageDescription(ChameleonReportable message) {
    return String.format(
      "%s in %s: %d|%d -> %s",
      message.messageType(),
      message.getLocationIdentifier(), 
      message.getLine(),
      message.getCharIndex(),
      message.getMessage()
    );
  }
}
