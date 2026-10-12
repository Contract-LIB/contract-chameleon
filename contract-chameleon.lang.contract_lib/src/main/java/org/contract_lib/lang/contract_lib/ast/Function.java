package org.contract_lib.lang.contract_lib.ast;

import java.util.List;

public interface Function {

  public <R> R perform(
      java.util.function.Function<FunctionDec, R> functionDec,
      java.util.function.Function<FunctionDef, R> functionDef);

  public record FunctionDec(
      Symbol name,
      List<Sort> arguments,
      Sort result) implements Function {

    @Override
    public <R> R perform(
        java.util.function.Function<FunctionDec, R> functionDec,
        java.util.function.Function<FunctionDef, R> functionDef) {
      return functionDec.apply(this);
    }
  }

  public record FunctionDef(
      Symbol name,
      List<SortedVar> arguments,
      Sort result,
      Term term) implements Function {

    @Override
    public <R> R perform(
        java.util.function.Function<FunctionDec, R> functionDec,
        java.util.function.Function<FunctionDef, R> functionDef) {
      return functionDef.apply(this);
    }
  }
}
