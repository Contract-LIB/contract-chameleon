package org.contract_lib.lang.contract_lib.ast;

import java.util.List;
import java.util.function.Function;

public record ContractLibAst(
    List<Datatype> datatypes,
    List<Abstraction> abstractions,
    List<SortDec> sorts,
    List<Parameter> sortParameter,

    List<FunctionDec> functions,
    List<Constant> constants,

    List<Contract> contracts,

    List<Assert> asserts) implements ContractLibAstElement {

  @Override
  public <R> R perform(
      Function<ContractLibAst, R> ast,
      Function<Command, R> command,
      Function<Sort, R> sort,
      Function<Term, R> term,
      Function<Inner, R> inner) {
    return ast.apply(this);
  }
}
