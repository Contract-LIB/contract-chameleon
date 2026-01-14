package org.contract_lib.lang.verifast.ast;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface VeriFastType {

  <R> R perform(
      Function<VeriFastBoolean, R> booleanType,
      Function<VeriFastInteger, R> integerType,
      Function<VeriFastClass, R> classType,
      Function<VeriFastInduction, R> inductionType);

  boolean isLogicType();

  String getName();

  public record VeriFastBoolean() implements VeriFastType {

    @Override
    public <R> R perform(
        Function<VeriFastBoolean, R> booleanType,
        Function<VeriFastInteger, R> integerType,
        Function<VeriFastClass, R> classType,
        Function<VeriFastInduction, R> inductionType) {
      return booleanType.apply(this);
    }

    @Override
    public boolean isLogicType() {
      return true;
    }

    @Override
    public String getName() {
      return "boolean";
    }
  }

  public record VeriFastInteger() implements VeriFastType {

    @Override
    public <R> R perform(
        Function<VeriFastBoolean, R> booleanType,
        Function<VeriFastInteger, R> integerType,
        Function<VeriFastClass, R> classType,
        Function<VeriFastInduction, R> inductionType) {
      return integerType.apply(this);
    }

    @Override
    public boolean isLogicType() {
      return true;
    }

    @Override
    public String getName() {
      return "int";
    }
  }

  public record VeriFastClass(String name, List<VeriFastType> genericArguments) implements VeriFastType {

    @Override
    public <R> R perform(
        Function<VeriFastBoolean, R> booleanType,
        Function<VeriFastInteger, R> integerType,
        Function<VeriFastClass, R> classType,
        Function<VeriFastInduction, R> inductionType) {
      return classType.apply(this);
    }

    @Override
    public boolean isLogicType() {
      return false;
    }

    @Override
    public String getName() {
      if (!genericArguments.isEmpty()) {
        return this.name + "<" + genericArguments.stream()
            .map(VeriFastType::getName)
            .collect(Collectors.joining(", "))
            + ">";
      }
      return this.name;
    }
  }

  public record VeriFastInduction(String name, List<VeriFastType> genericArguments) implements VeriFastType {

    @Override
    public <R> R perform(
        Function<VeriFastBoolean, R> booleanType,
        Function<VeriFastInteger, R> integerType,
        Function<VeriFastClass, R> classType,
        Function<VeriFastInduction, R> inductionType) {
      return inductionType.apply(this);
    }

    @Override
    public boolean isLogicType() {
      return true;
    }

    @Override
    public String getName() {
      if (!genericArguments.isEmpty()) {
        return this.name + "<" + genericArguments.stream()
            .map(VeriFastType::getName)
            .collect(Collectors.joining(", "))
            + ">";
      }
      return this.name;
    }
  }
}
