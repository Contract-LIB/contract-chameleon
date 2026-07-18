package org.contract_lib.lang.contract_lib.error;

import org.contract_lib.contract_chameleon.error.ChameleonError;
import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;
import org.contract_lib.lang.contract_lib.contexts.CurrentFileIdentifierContext;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.FilePositionLinkerContext;
import org.contract_lib.lang.contract_lib.label.IdentifierType;

public record IdentifierError(
    IdentifierErrorKind kind,
    String identifierValue,
    IdentifierType identifierType,
    ContractLibAstElement astNode,
    FilePositionLinkerContext filePosition,
    CurrentFileIdentifierContext fileIdentifier) implements ChameleonError {

  public String getLocationIdentifier() {
    return fileIdentifier.getFileIdentifier();
  }

  public int getLine() {
    return filePosition.getFilePosition(astNode).get().start().line();
  }

  public int getCharIndex() {
    return filePosition.getFilePosition(astNode).get().start().charIndex();
  }

  public String getMessage() {
    return kind.getMessage(identifierValue, identifierType);
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
