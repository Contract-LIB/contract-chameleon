
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
    System.err.println();
    System.err.println(new ChameleonMessageGroup(messages).getMessage());
  }

  /// Check that there are no reported messages
  public void check() throws ChameleonMessageGroup {
    if (!messages.isEmpty()) {
      throw new ChameleonMessageGroup(messages);
    }
  }
}
