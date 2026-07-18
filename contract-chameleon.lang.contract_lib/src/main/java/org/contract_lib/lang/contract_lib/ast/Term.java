package org.contract_lib.lang.contract_lib.ast;

import java.util.List;
import java.util.function.Function;

public interface Term extends ContractLibAstElement {

  public <R> R perform(
      Function<Term.SpecConstant, R> specConstant,
      Function<Term.Identifier.IdentifierAs, R> identifierAs,
      Function<Term.Identifier.IdentifierValue, R> identifierValue,
      Function<Term.Identifier.MethodApplication, R> methodApplication,
      Function<Term.Identifier.Old, R> old,
      Function<Term.Identifier.BooleanLiteral, R> booleanLiteral,
      Function<Term.Identifier.NumberLiteral, R> numberLiteral,
      Function<Term.Identifier.LetBinding, R> letBinding,
      Function<Term.Identifier.QuantorBinding, R> quantorBinding,
      Function<Term.Identifier.MatchBinding, R> matchBinding,
      Function<Term.Identifier.Attributes, R> attributes);

  @Override
  public default <R> R perform(
      Function<ContractLibAst, R> ast,
      Function<Command, R> command,
      Function<Sort, R> sort,
      Function<Term, R> term,
      Function<Inner, R> inner) {
    return term.apply(this);
  }

  public record SpecConstant(
      String value) implements Term {
    @Override
    public <R> R perform(
        Function<Term.SpecConstant, R> specConstant,
        Function<Term.Identifier.IdentifierAs, R> identifierAs,
        Function<Term.Identifier.IdentifierValue, R> identifierValue,
        Function<Term.Identifier.MethodApplication, R> methodApplication,
        Function<Term.Identifier.Old, R> old,
        Function<Term.Identifier.BooleanLiteral, R> booleanLiteral,
        Function<Term.Identifier.NumberLiteral, R> numberLiteral,
        Function<Term.Identifier.LetBinding, R> letBinding,
        Function<Term.Identifier.QuantorBinding, R> quantorBinding,
        Function<Term.Identifier.MatchBinding, R> matchBinding,
        Function<Term.Identifier.Attributes, R> attributes) {
      return specConstant.apply(this);
    }
  }

  public interface Identifier extends Term {
    IdentifierValue getValue();

    // Record, which explicitly stores the sort of the identifier
    public record IdentifierAs(
        IdentifierValue identifier,
        Sort sort) implements Identifier {
      @Override
      public <R> R perform(
          Function<Term.SpecConstant, R> specConstant,
          Function<Term.Identifier.IdentifierAs, R> identifierAs,
          Function<Term.Identifier.IdentifierValue, R> identifierValue,
          Function<Term.Identifier.MethodApplication, R> methodApplication,
          Function<Term.Identifier.Old, R> old,
          Function<Term.Identifier.BooleanLiteral, R> booleanLiteral,
          Function<Term.Identifier.NumberLiteral, R> numberLiteral,
          Function<Term.Identifier.LetBinding, R> letBinding,
          Function<Term.Identifier.QuantorBinding, R> quantorBinding,
          Function<Term.Identifier.MatchBinding, R> matchBinding,
          Function<Term.Identifier.Attributes, R> attributes) {
        return identifierAs.apply(this);
      }

      @Override
      public IdentifierValue getValue() {
        return this.identifier();
      }
    }

    public record IdentifierValue(
        Symbol identifier) implements Identifier {
      @Override
      public <R> R perform(
          Function<Term.SpecConstant, R> specConstant,
          Function<Term.Identifier.IdentifierAs, R> identifierAs,
          Function<Term.Identifier.IdentifierValue, R> identifierValue,
          Function<Term.Identifier.MethodApplication, R> methodApplication,
          Function<Term.Identifier.Old, R> old,
          Function<Term.Identifier.BooleanLiteral, R> booleanLiteral,
          Function<Term.Identifier.NumberLiteral, R> numberLiteral,
          Function<Term.Identifier.LetBinding, R> letBinding,
          Function<Term.Identifier.QuantorBinding, R> quantorBinding,
          Function<Term.Identifier.MatchBinding, R> matchBinding,
          Function<Term.Identifier.Attributes, R> attributes) {
        return identifierValue.apply(this);
      }

      @Override
      public IdentifierValue getValue() {
        return this;
      }
    }
  }

  public record MethodApplication(
      Identifier identifier,
      List<Term> parameters) implements Term {
    public <R> R perform(
        Function<Term.SpecConstant, R> specConstant,
        Function<Term.Identifier.IdentifierAs, R> identifierAs,
        Function<Term.Identifier.IdentifierValue, R> identifierValue,
        Function<Term.Identifier.MethodApplication, R> methodApplication,
        Function<Term.Identifier.Old, R> old,
        Function<Term.Identifier.BooleanLiteral, R> booleanLiteral,
        Function<Term.Identifier.NumberLiteral, R> numberLiteral,
        Function<Term.Identifier.LetBinding, R> letBinding,
        Function<Term.Identifier.QuantorBinding, R> quantorBinding,
        Function<Term.Identifier.MatchBinding, R> matchBinding,
        Function<Term.Identifier.Attributes, R> attributes) {
      return methodApplication.apply(this);
    }
  }

  public record BooleanLiteral(
      boolean value) implements Term {
    public <R> R perform(
        Function<Term.SpecConstant, R> specConstant,
        Function<Term.Identifier.IdentifierAs, R> identifierAs,
        Function<Term.Identifier.IdentifierValue, R> identifierValue,
        Function<Term.Identifier.MethodApplication, R> methodApplication,
        Function<Term.Identifier.Old, R> old,
        Function<Term.Identifier.BooleanLiteral, R> booleanLiteral,
        Function<Term.Identifier.NumberLiteral, R> numberLiteral,
        Function<Term.Identifier.LetBinding, R> letBinding,
        Function<Term.Identifier.QuantorBinding, R> quantorBinding,
        Function<Term.Identifier.MatchBinding, R> matchBinding,
        Function<Term.Identifier.Attributes, R> attributes) {
      return booleanLiteral.apply(this);
    }
  }

  //TODO: not int
  public record NumberLiteral(
      String value) implements Term {
    public <R> R perform(
        Function<Term.SpecConstant, R> specConstant,
        Function<Term.Identifier.IdentifierAs, R> identifierAs,
        Function<Term.Identifier.IdentifierValue, R> identifierValue,
        Function<Term.Identifier.MethodApplication, R> methodApplication,
        Function<Term.Identifier.Old, R> old,
        Function<Term.Identifier.BooleanLiteral, R> booleanLiteral,
        Function<Term.Identifier.NumberLiteral, R> numberLiteral,
        Function<Term.Identifier.LetBinding, R> letBinding,
        Function<Term.Identifier.QuantorBinding, R> quantorBinding,
        Function<Term.Identifier.MatchBinding, R> matchBinding,
        Function<Term.Identifier.Attributes, R> attributes) {
      return numberLiteral.apply(this);
    }
  }

  public record LetBinding(
      List<VarBinding> varbindings,
      Term term) implements Term {
    public <R> R perform(
        Function<Term.SpecConstant, R> specConstant,
        Function<Term.Identifier.IdentifierAs, R> identifierAs,
        Function<Term.Identifier.IdentifierValue, R> identifierValue,
        Function<Term.Identifier.MethodApplication, R> methodApplication,
        Function<Term.Identifier.Old, R> old,
        Function<Term.Identifier.BooleanLiteral, R> booleanLiteral,
        Function<Term.Identifier.NumberLiteral, R> numberLiteral,
        Function<Term.Identifier.LetBinding, R> letBinding,
        Function<Term.Identifier.QuantorBinding, R> quantorBinding,
        Function<Term.Identifier.MatchBinding, R> matchBinding,
        Function<Term.Identifier.Attributes, R> attributes) {
      return letBinding.apply(this);
    }
  }

  public record QuantorBinding(
      Quantor quantor,
      List<SortedVar> formals,
      Term term) implements Term {
    public <R> R perform(
        Function<Term.SpecConstant, R> specConstant,
        Function<Term.Identifier.IdentifierAs, R> identifierAs,
        Function<Term.Identifier.IdentifierValue, R> identifierValue,
        Function<Term.Identifier.MethodApplication, R> methodApplication,
        Function<Term.Identifier.Old, R> old,
        Function<Term.Identifier.BooleanLiteral, R> booleanLiteral,
        Function<Term.Identifier.NumberLiteral, R> numberLiteral,
        Function<Term.Identifier.LetBinding, R> letBinding,
        Function<Term.Identifier.QuantorBinding, R> quantorBinding,
        Function<Term.Identifier.MatchBinding, R> matchBinding,
        Function<Term.Identifier.Attributes, R> attributes) {
      return quantorBinding.apply(this);
    }
  }

  public record MatchBinding(
      Term term,
      List<MatchCase> matchCases) implements Term {
    public <R> R perform(
        Function<Term.SpecConstant, R> specConstant,
        Function<Term.Identifier.IdentifierAs, R> identifierAs,
        Function<Term.Identifier.IdentifierValue, R> identifierValue,
        Function<Term.Identifier.MethodApplication, R> methodApplication,
        Function<Term.Identifier.Old, R> old,
        Function<Term.Identifier.BooleanLiteral, R> booleanLiteral,
        Function<Term.Identifier.NumberLiteral, R> numberLiteral,
        Function<Term.Identifier.LetBinding, R> letBinding,
        Function<Term.Identifier.QuantorBinding, R> quantorBinding,
        Function<Term.Identifier.MatchBinding, R> matchBinding,
        Function<Term.Identifier.Attributes, R> attributes) {
      return matchBinding.apply(this);
    }
  }

  public record Attributes(
      Term term,
      List<String> attributes) implements Term {
    public <R> R perform(
        Function<Term.SpecConstant, R> specConstant,
        Function<Term.Identifier.IdentifierAs, R> identifierAs,
        Function<Term.Identifier.IdentifierValue, R> identifierValue,
        Function<Term.Identifier.MethodApplication, R> methodApplication,
        Function<Term.Identifier.Old, R> old,
        Function<Term.Identifier.BooleanLiteral, R> booleanLiteral,
        Function<Term.Identifier.NumberLiteral, R> numberLiteral,
        Function<Term.Identifier.LetBinding, R> letBinding,
        Function<Term.Identifier.QuantorBinding, R> quantorBinding,
        Function<Term.Identifier.MatchBinding, R> matchBinding,
        Function<Term.Identifier.Attributes, R> attributes) {
      return attributes.apply(this);
    }
  }

  record Old(
      Term argument) implements Term {
    public <R> R perform(
        Function<Term.SpecConstant, R> specConstant,
        Function<Term.Identifier.IdentifierAs, R> identifierAs,
        Function<Term.Identifier.IdentifierValue, R> identifierValue,
        Function<Term.Identifier.MethodApplication, R> methodApplication,
        Function<Term.Identifier.Old, R> old,
        Function<Term.Identifier.BooleanLiteral, R> booleanLiteral,
        Function<Term.Identifier.NumberLiteral, R> numberLiteral,
        Function<Term.Identifier.LetBinding, R> letBinding,
        Function<Term.Identifier.QuantorBinding, R> quantorBinding,
        Function<Term.Identifier.MatchBinding, R> matchBinding,
        Function<Term.Identifier.Attributes, R> attributes) {
      return old.apply(this);
    }
  }
}
