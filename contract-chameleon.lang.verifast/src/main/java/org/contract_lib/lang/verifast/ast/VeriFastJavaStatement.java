package org.contract_lib.lang.verifast.ast;

import java.util.List;
import java.util.function.Function;

public interface VeriFastJavaStatement {

  public <R> R perform(
      Function<ConstructorCall, R> constructor,
      Function<DefaultValue, R> defaultValue);

  public record ConstructorCall(
      VeriFastType classType,
      List<VeriFastArgument> arguments) implements VeriFastJavaStatement {

    @Override
    public <R> R perform(
        Function<ConstructorCall, R> constructor,
        Function<DefaultValue, R> defaultValue) {
      return constructor.apply(this);
    }
  }

  public record DefaultValue(VeriFastType type) implements VeriFastJavaStatement {
    @Override
    public <R> R perform(
        Function<ConstructorCall, R> constructor,
        Function<DefaultValue, R> defaultValue) {
      return defaultValue.apply(this);
    }
  }
}
