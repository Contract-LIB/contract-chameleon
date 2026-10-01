
package org.contract_lib.lang.contract_lib.contexts;

import java.util.Optional;

import org.antlr.v4.runtime.CharStream;
import org.contract_lib.contract_chameleon.SharedContextManager.SettableContext;

public class CharStreamContext implements SettableContext {

  private Optional<CharStream> charStream;

  public CharStreamContext(Optional<CharStream> fileIdentifier) {
    this.charStream = fileIdentifier;
  }

  public Optional<CharStream> getCharStream() {
    return charStream;
  }

  public void setCharStream(CharStream charStream) {
    this.charStream = Optional.ofNullable(charStream);
  }
}
