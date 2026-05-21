package org.contract_lib.lang.contract_lib.ast;

import java.util.List;
import java.util.function.Function;

public interface Sort extends ContractLibAstElement {
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
