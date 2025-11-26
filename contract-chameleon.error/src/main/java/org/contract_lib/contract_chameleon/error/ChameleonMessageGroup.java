package org.contract_lib.contract_chameleon.error;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/// A group of messages that have a connection.
public final class ChameleonMessageGroup extends Exception {

  private final List<ChameleonReportable> messages;

  /// Create a new message group from a list.
  public ChameleonMessageGroup(List<ChameleonReportable> messages) {
    this.messages = new ArrayList<>(messages);
  }

  /// Get a descirption of all messages
  public String getMessage() {
    return this.messages.stream().sorted()
        .map(this::messageDescription)
        .collect(Collectors.joining(System.lineSeparator()));
  }

  /// Get all messages.
  public List<ChameleonReportable> getMessages() {
    return new ArrayList<>(this.messages);
  }

  private String messageDescription(ChameleonReportable message) {
    return String.format(
        "%s in %s: %d|%d -> %s",
        message.messageType(),
        message.getLocationIdentifier(),
        message.getLine(),
        message.getCharIndex(),
        message.getMessage());
  }
}
