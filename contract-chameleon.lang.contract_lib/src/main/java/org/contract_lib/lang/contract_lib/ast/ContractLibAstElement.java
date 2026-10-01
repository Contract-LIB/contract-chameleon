package org.contract_lib.lang.contract_lib.ast;

import java.util.function.Function;

/// The most general ast element.
public interface ContractLibAstElement {

  public <R> R perform(
      Function<ContractLibAst, R> ast,
      Function<Command, R> command,
      Function<Sort, R> sort,
      Function<Term, R> term,
      Function<Inner, R> inner);

  /// A node that is part of an bigger syntactic construct.
  public interface Inner extends ContractLibAstElement {
    @Override
    public default <R> R perform(
        Function<ContractLibAst, R> ast,
        Function<Command, R> command,
        Function<Sort, R> sort,
        Function<Term, R> term,
        Function<Inner, R> inner) {
      return inner.apply(this);
    }
  }
}
