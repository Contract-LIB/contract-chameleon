package org.contract_lib.contract_chameleon.error;

import java.util.Optional;

/** Error case when there are labels for an argument but none are expected.
 */
public final class SimpleErrorMessage implements ChameleonReportable {

  private String message;
  private ChameleonMessageType type;
  private Optional<String> detailedMessage;

  /** Simplyfied error constructor with custom type {@ChameleonMessageType.ERROR} and detailed message.
   *
   * Pass {@code null} to the detailed message if you don't want to provide one.
   */
  public SimpleErrorMessage(
      String message,
      ChameleonMessageType type,
      String detailedMessage) {
    this.message = message;
    this.type = type;
    this.detailedMessage = Optional.ofNullable(detailedMessage);
  }

  /** Simplyfied error constructor of type {@ChameleonMessageType.ERROR} with no detailed message.
   */
  public SimpleErrorMessage(
      String message) {
    this.message = message;
    this.type = ChameleonMessageType.ERROR;
    this.detailedMessage = Optional.empty();
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public ChameleonMessageType messageType() {
    return type;
  }

  @Override
  public Optional<String> getDetailedMessage() {
    return detailedMessage;
  }
}
