package org.contract_lib.lang.contract_lib.modifier;

import org.contract_lib.contract_chameleon.error.ChameleonMessageManager;
import org.contract_lib.lang.contract_lib.ast.Contract;
import org.contract_lib.lang.contract_lib.ast.Formal;
import org.contract_lib.lang.contract_lib.ast.MatchCase;
import org.contract_lib.lang.contract_lib.ast.Pattern;
import org.contract_lib.lang.contract_lib.ast.PrePostPair;
import org.contract_lib.lang.contract_lib.ast.SortedVar;
import org.contract_lib.lang.contract_lib.ast.Symbol;
import org.contract_lib.lang.contract_lib.ast.Term;
import org.contract_lib.lang.contract_lib.ast.VarBinding;
import org.contract_lib.lang.contract_lib.ast.Pattern.Case;
import org.contract_lib.lang.contract_lib.ast.Pattern.WithParameters;
import org.contract_lib.lang.contract_lib.ast.Term.Old;
import org.contract_lib.lang.contract_lib.ast.Term.QuantorBinding;
import org.contract_lib.lang.contract_lib.ast.Term.BooleanLiteral;
import org.contract_lib.lang.contract_lib.ast.Term.LetBinding;
import org.contract_lib.lang.contract_lib.ast.Term.MatchBinding;
import org.contract_lib.lang.contract_lib.ast.Term.MethodApplication;
import org.contract_lib.lang.contract_lib.ast.Term.NumberLiteral;
import org.contract_lib.lang.contract_lib.ast.Term.SpecConstant;
import org.contract_lib.lang.contract_lib.ast.Term.Attributes;
import org.contract_lib.lang.contract_lib.ast.Term.Identifier.IdentifierAs;
import org.contract_lib.lang.contract_lib.ast.Term.Identifier.IdentifierValue;
import org.contract_lib.lang.contract_lib.error.SubstitutionError;
import org.contract_lib.lang.contract_lib.modifier.substitution.ShouldSubstituteInterface;
import org.contract_lib.lang.contract_lib.modifier.substitution.Substitution;
import org.contract_lib.lang.contract_lib.modifier.substitution.SubstitutionConflict;

public class IdentifierSubstitution {

  private final ShouldSubstituteInterface<Symbol> shouldSubstitute;
  private final SubstitutionConflict<Symbol> substitutionConflict;
  private final Substitution<Symbol> substitution;

  private final ChameleonMessageManager messageManager;

  public IdentifierSubstitution(
      String identifier,
      String with,
      ChameleonMessageManager messageManager) {

    //TODO: add access to the position of the element in the file,…
    //update labels of the new created elements

    //TODO: check that `with` does not conflic with any predefined
    //method names, constant identifiers,…

    this.shouldSubstitute = e -> e.identifier()
        .equals(identifier);

    this.substitution = e -> e.identifier()
        .equals(identifier) ? new Symbol(with) : e;

    this.substitutionConflict = (e, m) -> {
      if (e.identifier().equals(with)) {
        m.report(
            //TODO: add access to the position fo the element
            new SubstitutionError("undef", 0, 0,
                String.format("The element %s could not be substituted with %s",
                    e.identifier(),
                    with)));
      }
    };

    this.messageManager = messageManager;
  }

  public Symbol applySymbolSubstition(Symbol element) {

    substitutionConflict.substitutionConflict(element, messageManager);

    if (shouldSubstitute.shouldSubstitute(element)) {
      return substitution.substitue(element);
    } else {
      return element;
    }
  }

  public Symbol blockSymbolSubstition(Symbol element) {
    substitutionConflict.substitutionConflict(element, messageManager);
    return element;
  }

  public Contract applyContract(Contract element) {
    return new Contract(
        this.blockSymbolSubstition(element.identifier()),
        element.formals().stream().map(this::applyFormal).toList(),
        element.pairs().stream().map(this::applyPrePostPair).toList());
  }

  public Formal applyFormal(Formal formal) {
    return new Formal(
        this.applySymbolSubstition(formal.identifier()),
        formal.argumentMode(),
        formal.sort());
  }

  public PrePostPair applyPrePostPair(PrePostPair prePostPair) {
    return new PrePostPair(
        this.apply(prePostPair.pre()),
        this.apply(prePostPair.post()));
  }

  public Term apply(Term element) {
    return element.perform(
        this::applySpecificConstant,
        this::applyIdentifierAs,
        this::applyIdentifierValue,
        this::applyMethodApplication,
        this::applyOld,
        this::applyBooleanLiteral,
        this::applyNumberLiteral,
        this::applyLetBinding,
        this::applyQuantorBinding,
        this::applyMatchBinding,
        this::applyAttributes);
  }

  public SpecConstant applySpecificConstant(SpecConstant element) {
    return element;
  }

  public IdentifierValue applyIdentifierValue(IdentifierValue element) {
    return new IdentifierValue(this.applySymbolSubstition(element.identifier()));
  }

  public IdentifierAs applyIdentifierAs(IdentifierAs element) {
    return new IdentifierAs(this.applyIdentifierValue(element.identifier()), element.sort());
  }

  public MethodApplication applyMethodApplication(MethodApplication element) {
    return new MethodApplication(
        this.applyIdentifierValue(element.identifier().getValue()),
        element.parameters().stream().map(this::apply).toList());
  }

  public Old applyOld(Old element) {
    return new Old(this.apply(element.argument()));
  }

  public BooleanLiteral applyBooleanLiteral(BooleanLiteral element) {
    return element;
  }

  public NumberLiteral applyNumberLiteral(NumberLiteral element) {
    return element;
  }

  public LetBinding applyLetBinding(LetBinding element) {
    return new LetBinding(
        element.varbindings().stream().map(this::applyVarBinding).toList(),
        this.apply(element.term()));
  }

  public VarBinding applyVarBinding(VarBinding element) {
    return new VarBinding(this.blockSymbolSubstition(element.name()), this.apply(element.type()));
  }

  public SortedVar applySortedVar(SortedVar element) {
    return new SortedVar(this.blockSymbolSubstition(element.symbol()), element.sort());
  }

  public QuantorBinding applyQuantorBinding(QuantorBinding element) {
    return new QuantorBinding(
        element.quantor(),
        element.formals().stream().map(this::applySortedVar).toList(),
        this.apply(element.term()));
  }

  public MatchBinding applyMatchBinding(MatchBinding matchBinding) {
    return new MatchBinding(this.apply(matchBinding.term()),
        matchBinding.matchCases().stream().map(this::applyMatchCase).toList());

  }

  public MatchCase applyMatchCase(MatchCase matchBinding) {
    return new MatchCase(this.applyPattern(matchBinding.pattern()), this.apply(matchBinding.term()));
  }

  public Pattern applyPattern(Pattern matchCase) {
    return matchCase.perform(this::applyCase, this::applyWithParameters);
  }

  public Case applyCase(Case matchCase) {
    return new Case(this.blockSymbolSubstition(matchCase.symbol()));
  }

  public WithParameters applyWithParameters(WithParameters withParameters) {
    return new WithParameters(
        this.blockSymbolSubstition(withParameters.symbol()),
        withParameters.parameters().stream().map(this::blockSymbolSubstition).toList());
  }

  public Attributes applyAttributes(Attributes attributes) {
    return new Attributes(
        this.apply(attributes.term()),
        attributes.attributes());
  }
}
