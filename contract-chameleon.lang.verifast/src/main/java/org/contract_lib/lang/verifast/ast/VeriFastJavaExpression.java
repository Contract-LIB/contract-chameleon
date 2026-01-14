package org.contract_lib.lang.verifast.ast;

import java.util.function.Function;

public interface VeriFastJavaExpression {


  public <R> R perform(
      Function<Standard, R> standardExpression,
      Function<Return, R> returnExpression,
      Function<SimpleStatement, R> simpleStatement);


  public record Standard(
      VeriFastJavaStatement statement) implements VeriFastJavaExpression {

    @Override
      public <R> R perform(
      Function<Standard, R> standardExpression,
      Function<Return, R> returnExpression,
      Function<SimpleStatement, R> simpleStatement) { 
      return standardExpression.apply(this);
    }
  }
  public record Return(
      VeriFastJavaStatement statement) implements VeriFastJavaExpression {

    @Override
      public <R> R perform(
        Function<Standard, R> standard,
        Function<Return, R> returnExpression,
        Function<SimpleStatement, R> simpleStatement) { 
      return returnExpression.apply(this);
    }
  }

    public record SimpleStatement(
      String statement 
    ) implements VeriFastJavaExpression {

      @Override
      public <R> R perform(
      Function<Standard, R> standardExpression,
      Function<Return, R> returnExpression,
      Function<SimpleStatement, R> simpleStatement) { 
          return simpleStatement.apply(this);
      }
    }
}
