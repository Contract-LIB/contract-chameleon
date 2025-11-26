package org.contract_lib.contract_chameleon.error;

/// Type of a message, that is shown to the user by {@ChameleonReportable}.
public enum ChameleonMessageType {

  /// There is an invalid state, continuation is not possible.
  ERROR,
  /// There is an error, which can be in erraneous in some cases.
  WARNING,
  /// Information to the user.
  INFO,
  /// Detailed debug information.
  DEBUG;

  @Override
  public String toString() {
    return switch (this) {
      case ERROR -> "ERROR";
      case WARNING -> "WARNING";
      case INFO -> "INFO";
      case DEBUG -> "DEBUG";
    };
  }
}
