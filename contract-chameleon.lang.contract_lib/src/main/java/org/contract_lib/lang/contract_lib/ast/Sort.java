package org.contract_lib.lang.contract_lib.ast;

import java.util.List;
import java.util.function.Function;

// TODO: Rename to `SortTerm`, do make difference
public interface Sort extends ContractLibAstElement {

  @Override
  public default <R> R perform(
      Function<ContractLibAst, R> ast,
      Function<Command, R> command,
      Function<Sort, R> sort,
      Function<Term, R> term,
      Function<Inner, R> inner) {
    return sort.apply(this);
  }

  // TODO: Rename to `Sort`
  public record Type(
      String name) implements Sort {
    public <R> R perform(
        Function<Sort.Type, R> type,
        Function<Sort.ParametricType, R> parametricType) {
      return type.apply(this);
    }

    public String getName() {
      return name;
    }
  }

  // TODO: Rename to `ParametricSort`
  public record ParametricType(
      String name,
      List<Sort> arguments) implements Sort {
    public <R> R perform(
        Function<Sort.Type, R> type,
        Function<Sort.ParametricType, R> parametricType) {
      return parametricType.apply(this);
    }

    public String getName() {
      return name;
    }
  }

  public <R> R perform(
      Function<Sort.Type, R> type,
      Function<Sort.ParametricType, R> parametricType);

  public String getName();
}
