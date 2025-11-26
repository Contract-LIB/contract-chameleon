package org.contract_lib.lang.contract_lib.generator;

import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.BaseErrorListener;

import org.contract_lib.contract_chameleon.error.ChameleonMessageManager;

import org.contract_lib.lang.contract_lib.error.SyntaxError;

final class ContractLibAstErrorListener extends BaseErrorListener {

  private final ChameleonMessageManager manager;
  private final String locationId;

  ContractLibAstErrorListener(
      String locationId,
      ChameleonMessageManager manager) {
    this.locationId = locationId;
    this.manager = manager;
  }

  @Override
  public void syntaxError(
      Recognizer<?, ?> recognizer,
      Object offendingSymbol,
      int line,
      int charPositionInLine,
      String msg,
      RecognitionException e) {
    manager.report(new SyntaxError(locationId, line, charPositionInLine, msg, ""));
  }
}
