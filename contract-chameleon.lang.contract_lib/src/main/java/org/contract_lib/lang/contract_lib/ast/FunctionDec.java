package org.contract_lib.lang.contract_lib.ast;

import java.util.List;
import java.util.function.Function;

public record FunctionDec(
    Symbol name,
    List<Parameter> params,
    List<Sort> arguments,
    Sort result) implements JoinableCommand<FunctionDec> {

  @Override
  public <R> R perform(
      Function<Abstraction, R> decAbstraction,
      Function<JoinedCommand<Abstraction>, R> decAbstractions,
      Function<Constant, R> decConstant,
      Function<Datatype, R> decDatatype,
      Function<JoinedCommand<Datatype>, R> decDatatypes,
      Function<FunctionDec, R> decFunction,
      Function<Parameter, R> decParameter,
      Function<SortDec, R> decSort,
      Function<SortDec, R> defSort,
      Function<Contract, R> defContract,
      Function<FunctionDec, R> defFunction,
      Function<FunctionDec, R> defFunctionRec,
      Function<JoinedCommand<FunctionDec>, R> defFunctionsRec,
      Function<Assert, R> assertion) {
    return decFunction.apply(this);
  }

  @Override
  public <R> Function<JoinedCommand<FunctionDec>, R> select(
      Function<JoinedCommand<Abstraction>, R> decAbstractions,
      Function<JoinedCommand<Datatype>, R> decDatatape,
      Function<JoinedCommand<FunctionDec>, R> decFunctionsRec) {
    return decFunctionsRec;
  }
}
