
package org.contract_lib.contract_chameleon.error;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;

/// Manager for messages that are shown to the user.
public final class ChameleonMessageManager {

  private final List<ChameleonReportable> messages = new ArrayList<>();

  /// Report a new message.
  public void report(ChameleonReportable message) {
    messages.add(message);
  }

  /// Get a sorted stream of all messages.
  public Stream<ChameleonReportable> getMessages() {
    return messages.stream().sorted();
  }

  /// Write all reported messages to std error.
  public void writeStdErr() {
    messages.stream()
        .map(c -> String.format("%s: %s", c.messageType(), c.getMessage()))
        .forEachOrdered(System.err::println);
  }

  /// Check that there are no reported messages
  public void check() throws Exception {
    //TODO: Provide better interface for `ChameleonMessageManager`.
    if (!messages.isEmpty()) {
      throw new Exception("Message");
    }
  }
}
