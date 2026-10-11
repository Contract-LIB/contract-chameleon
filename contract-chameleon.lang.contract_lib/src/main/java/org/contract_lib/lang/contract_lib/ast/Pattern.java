
package org.contract_lib.lang.contract_lib.ast;

import java.util.List;
import java.util.function.Function;

import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement.Inner;

public interface Pattern extends Inner {

  <R> R perform(Function<Case, R> patternCase,
      Function<WithParameters, R> patternWithParameters);

  public record Case(
      Symbol symbol) implements Pattern {

    public <R> R perform(Function<Case, R> patternCase,
        Function<WithParameters, R> patternWithParameters) {
      return patternCase.apply(this);
    }
  }

  public record WithParameters(
      Symbol symbol,
      List<Symbol> parameters) implements Pattern {

    public <R> R perform(Function<Case, R> patternCase,
        Function<WithParameters, R> patternWithParameters) {
      return patternWithParameters.apply(this);
    }
  }
}
