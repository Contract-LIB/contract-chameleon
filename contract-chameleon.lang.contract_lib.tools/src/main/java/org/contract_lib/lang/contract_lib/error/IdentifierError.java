package org.contract_lib.lang.contract_lib.error;

import org.contract_lib.contract_chameleon.error.ChameleonError;
import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;
import org.contract_lib.lang.contract_lib.contexts.CurrentFileIdentifierContext;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.FilePositionLinkerContext;
import org.contract_lib.lang.contract_lib.label.IdentifierType;

public final record IdentifierError(
    String file,
    int line,
    int charIndex,
    String message) implements ChameleonError {

  public IdentifierError(
      IdentifierErrorKind kind,
      String identifierValue,
      IdentifierType identifierType,
      ContractLibAstElement astNode,
      FilePositionLinkerContext filePosition,
      CurrentFileIdentifierContext fileIdentifier) {
    this(
        fileIdentifier.getFileIdentifier(),
        filePosition.getFilePosition(astNode).get().start().line(),
        filePosition.getFilePosition(astNode).get().start().charIndex(),
        kind.getMessage(identifierValue, identifierType));
  }

  public IdentifierError(
      IdentifierErrorKind kind,
      String identifierValue,
      IdentifierType identifierType,
      String fileIdentifier,
      int line,
      int charIndex) {
    this(
        fileIdentifier,
        line,
        charIndex,
        kind.getMessage(identifierValue, identifierType));
  }

  public String getLocationIdentifier() {
    return this.file;
  }

  public int getLine() {
    return this.line;
  }

  public int getCharIndex() {
    return this.charIndex;
  }

  public String getMessage() {
    return this.message;
  }

  public static enum IdentifierErrorKind {

    REDEFINED("The %s identifier %s was redefined."),
    UNDEFINED("The %s identifier %s is undefined.");

    private String message;

    private IdentifierErrorKind(String message) {
      this.message = message;
    }

    public String getMessage(String identifierValue, IdentifierType identifierType) {
      return String.format(
          this.message,
          identifierType.getMessageDescription(),
          identifierValue);
    }
  }
}
