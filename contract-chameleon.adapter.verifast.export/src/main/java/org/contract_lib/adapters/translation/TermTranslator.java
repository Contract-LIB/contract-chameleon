package org.contract_lib.adapters.translation;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import java.util.stream.Collectors;

import org.contract_lib.contract_chameleon.error.ChameleonMessageManager;
import org.contract_lib.contract_chameleon.contexts.MessageContext;

import org.contract_lib.lang.contract_lib.ast.Term;
import org.contract_lib.lang.contract_lib.ast.PrePostPair;
import org.contract_lib.lang.contract_lib.ast.Quantor;
import org.contract_lib.lang.contract_lib.ast.SortedVar;
import org.contract_lib.lang.contract_lib.ast.Argument;
import org.contract_lib.lang.contract_lib.ast.Sort;

import org.contract_lib.lang.verifast.ast.VeriFastExpression;
import org.contract_lib.lang.verifast.ast.VeriFastArgument;
import org.contract_lib.lang.verifast.ast.VeriFastType;

public final class TermTranslator {

  private final Map<String, TermTranslation> translations;

  private final MessageContext messageContext;

  private final TypeTranslator typeTranslator;
  private final QuantorTranslator quantorTranslator;
  private final PredicateTranslator predTranslator;
  private final HelperTranslator helperTranslator;

  private boolean accessOld;

  public TermTranslator(
      List<TermTranslation> termTranslations,
      List<QuantorTranslation> quantorTranslations,
      TypeTranslator typeTranslator,
      PredicateTranslator predTranslator,
      HelperTranslator helperTranslator,
      MessageContext messageContext) {
    this.accessOld = true;
    this.translations = new HashMap();

    this.messageContext = messageContext;

    this.typeTranslator = typeTranslator;
    this.predTranslator = predTranslator;
    this.helperTranslator = helperTranslator;

    this.quantorTranslator = new QuantorTranslator(
        quantorTranslations,
        this.messageContext.getMessageManager(),
        this,
        this.typeTranslator,
        this.predTranslator,
        this.helperTranslator);

    termTranslations.stream()
        .forEach(this::addTranslation);
  }

  public void addTranslation(TermTranslation translation) {
    String id = translation.getClibIdentifier().getValue().identifier().identifier();

    if (translations.get(id) != null) {
      messageContext.logWarning(String.format(
            "Identifier %s already exists as translation, be sure that the translations for all types are identical, as they are not matched by type signature yet!", id));
    }
    //if (translations.get(id) != null) {
    //  messageContext.logError(String.format("Identifier %s already exists as translation. unable to add %s", id, translation));
    //  return; 
    //}
      this.translations.put(translation.getClibIdentifier().getValue().identifier().identifier(), translation);
  }

  // - Translation of multiple contract terms

  public VeriFastPrePostExpression translateContractPairs(
      List<VeriFastExpression> predicateListPre,
      List<VeriFastExpression> predicateListPost,
      List<PrePostPair> pairs) {
    if (pairs.size() > 1) {
      System.err
          .println("ERROR: This adapter only supports contracts with one pair of pre / post condition at the moment.");
    }
    List<VeriFastPrePostExpression> exprPairs = pairs.stream()
        .map(p -> this.translatePrePostPair(predicateListPre, predicateListPost, p))
        .collect(Collectors.toList());

    //TODO: Chain with or not &*&
    List<VeriFastExpression> pre = exprPairs.stream()
        .map(VeriFastPrePostExpression::pre)
        .collect(Collectors.toList());

    //TODO: Chain with & with pre => post
    List<VeriFastExpression> post = exprPairs.stream()
        .map(VeriFastPrePostExpression::post)
        //.map(this::createPostExpression)
        .collect(Collectors.toList());

    return new VeriFastPrePostExpression(
        new VeriFastExpression.Chain(pre),
        new VeriFastExpression.Chain(post));
  }

  private VeriFastPrePostExpression translatePrePostPair(
      List<VeriFastExpression> predicateListPre,
      List<VeriFastExpression> predicateListPost,
      PrePostPair pair) {
    this.accessOld = true;
    VeriFastExpression pre = this.translateTerm(predicateListPre, pair.pre());
    this.accessOld = false;
    VeriFastExpression post = this.translateTerm(predicateListPost, pair.post());
    return new VeriFastPrePostExpression(pre, post);
  }

  // - Translation of Term

  public VeriFastExpression translateTerm(
      List<VeriFastExpression> predicateList,
      Term term) {
    return term.perform(
        (t) -> this.translateTermSpecConstant(predicateList, t),
        (t) -> this.translateTermIdentifierAs(predicateList, t),
        (t) -> this.translateTermIdentifierValue(predicateList, t),
        (t) -> this.translateTermMethodApplication(predicateList, t),
        (t) -> this.translateTermOld(predicateList, t),
        (t) -> this.translateTermBooleanLiteral(predicateList, t),
        (t) -> this.translateTermNumberLiteral(predicateList, t),
        (t) -> this.translateTermLetBinding(predicateList, t),
        (t) -> this.translateTermQuantorBinding(predicateList, t),
        (t) -> this.translateTermMatchBinding(predicateList, t),
        (t) -> this.translateTermAttributes(predicateList, t));
  }

  public void infoLogAvailableTranslations() {
    messageContext.logInfo(String.format("Avalable Term Translations for Identifier: %n%s",
    this.translations.keySet().stream().sorted().collect(Collectors.joining(System.lineSeparator()))));
  }

  private VeriFastExpression translateTermSpecConstant(
      List<VeriFastExpression> predicateList,
      Term.SpecConstant term) {
    messageContext.logError("Specific constants terms are not supported by this adapter yet.");
    return new VeriFastExpression.Variable("SPEC_CONST");
  }

  private VeriFastExpression translateTermIdentifierAs(
      List<VeriFastExpression> predicateList,
      Term.Identifier.IdentifierAs term) {
    return translateTermIdentifierValue(predicateList, term.identifier());
  }

  private VeriFastExpression translateTermIdentifierValue(
      List<VeriFastExpression> predicateList,
      Term.Identifier.IdentifierValue term) {
    TermTranslation t = this.translations.get(term.identifier().identifier());

    if (t == null) {
      messageContext.logError(
      String.format(
          "No translation for identifier %s found.",
          term));
      return new VeriFastExpression.BooleanValue(true);
    }

    if (t.getClibParameterTypes().size() != 0 && t.getVerifastParameterTypes().size() == 0) {
      messageContext.logError(
      String.format(
          "The term %s could not be translated as the translation requires %s paramters, but %s were provided.",
          term,
          t.getClibParameterTypes().size(), t.getVerifastParameterTypes().size()));

      return new VeriFastExpression.BooleanValue(true);
    }

    return t.getVerifastTerm(List.of());
  }

  private VeriFastExpression translateTermMethodApplication(
      List<VeriFastExpression> predicateList,
      Term.MethodApplication term) {

    TermTranslation t = this.translations.get(term.identifier().getValue().identifier().identifier());
    if (t != null) {
      List<VeriFastExpression> parameters = term.parameters()
          .stream()
          .map(par -> this.translateTerm(predicateList, par))
          .collect(Collectors.toList());

      return t.getVerifastTerm(parameters);
    }

    Optional<PredicateTranslation> trans = predTranslator.translatePredicate(term, this.accessOld);
    if (trans.isPresent()) {
      return trans.get().generateVariable();
    }

    System.err.println(String.format("Method applications for '%s' is not supported by this adapter yet.",
        term.identifier().getValue().identifier().identifier()));
    return new VeriFastExpression.Variable("METHOD_APPL");
  }

  private VeriFastExpression translateTermOld(
      List<VeriFastExpression> predicateList,
      Term.Old term) {
    Optional<PredicateTranslation> trans = predTranslator.translatePredicateOld(term);
    if (trans.isPresent()) {
      return trans.get().generateVariable();
    }
    System.err.println(String.format("Invalid old access in: ", term.argument()));
    return new VeriFastExpression.BooleanValue(true);
  }

  private VeriFastExpression translateTermBooleanLiteral(
      List<VeriFastExpression> predicateList,
      Term.BooleanLiteral term) {
    return new VeriFastExpression.BooleanValue(term.value());
  }

  private VeriFastExpression translateTermNumberLiteral(
      List<VeriFastExpression> predicateList,
      Term.NumberLiteral term) {
    return new VeriFastExpression.IntegerValue(term.value());
  }

  private VeriFastExpression translateTermLetBinding(
      List<VeriFastExpression> predicateList,
      Term.LetBinding term) {
    System.err.println("Let binding terms terms are not supported by this adapter yet.");
    return new VeriFastExpression.BooleanValue(true);
  }

  private VeriFastExpression translateTermQuantorBinding(
      List<VeriFastExpression> predicateList,
      Term.QuantorBinding term) {
    return quantorTranslator.translate(predicateList, term);
  }

  private VeriFastExpression translateTermMatchBinding(
      List<VeriFastExpression> predicateList,
      Term.MatchBinding term) {
    System.err.println("Match terms are not supported by this adapter yet.");
    //TODO: this might be handable with a switch in an helping fixpoint?
    return new VeriFastExpression.BooleanValue(true);
  }

  private VeriFastExpression translateTermAttributes(
      List<VeriFastExpression> predicateList,
      Term.Attributes term) {
    System.err.println("Attribute terms are not supported by this adapter yet.");
    return translateTerm(predicateList, term.term());
  }

  public record VeriFastPrePostExpression(
      VeriFastExpression pre,
      VeriFastExpression post) {
  }
}
