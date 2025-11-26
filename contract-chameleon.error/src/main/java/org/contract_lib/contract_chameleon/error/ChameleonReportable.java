
package org.contract_lib.contract_chameleon.error;

import java.util.Optional;
import java.util.Comparator;

/// Interface for messages of contract-chameleon.
public interface ChameleonReportable extends Comparable<ChameleonReportable> {
  /// The location (file path) where the message appears.
  String getLocationIdentifier();

  /// The line where the message appears.
  int getLine();

  /// The index in the line where the message appears.
  int getCharIndex();

  /// One line desciption of the message.
  String getMessage();

  /// Detailed desciption of the message.
  Optional<String> getDetailedMessage();

  /// The type of the message.
  ChameleonMessageType messageType();

  default int compareTo(ChameleonReportable o) {
    return Comparator.comparing(ChameleonReportable::getLocationIdentifier)
        .thenComparing(ChameleonReportable::getLine)
        .thenComparing(ChameleonReportable::getCharIndex)
        .thenComparing(ChameleonReportable::messageType)
        .compare(this, o);
  }
}
