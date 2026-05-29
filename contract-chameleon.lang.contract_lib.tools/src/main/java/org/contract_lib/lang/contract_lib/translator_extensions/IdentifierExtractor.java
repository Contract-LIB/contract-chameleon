package org.contract_lib.lang.contract_lib.translator_extensions;

import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser;
import org.contract_lib.lang.contract_lib.ast.Abstraction;
import org.contract_lib.lang.contract_lib.ast.Assert;
import org.contract_lib.lang.contract_lib.ast.Constant;
import org.contract_lib.lang.contract_lib.ast.Constructor;
import org.contract_lib.lang.contract_lib.ast.Contract;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;
import org.contract_lib.lang.contract_lib.ast.Datatype;
import org.contract_lib.lang.contract_lib.ast.Formal;
import org.contract_lib.lang.contract_lib.ast.FunctionDec;
import org.contract_lib.lang.contract_lib.ast.JoinedCommand;
import org.contract_lib.lang.contract_lib.ast.MatchCase;
import org.contract_lib.lang.contract_lib.ast.Numeral;
import org.contract_lib.lang.contract_lib.ast.Pattern;
import org.contract_lib.lang.contract_lib.ast.PrePostPair;
import org.contract_lib.lang.contract_lib.ast.SelectorDec;
import org.contract_lib.lang.contract_lib.ast.SortDec;
import org.contract_lib.lang.contract_lib.ast.SortedVar;
import org.contract_lib.lang.contract_lib.ast.VarBinding;
import org.contract_lib.lang.contract_lib.ast.Sort.ParametricType;
import org.contract_lib.lang.contract_lib.ast.Sort.Type;
import org.contract_lib.lang.contract_lib.ast.Term.NumberLiteral;
import org.contract_lib.lang.contract_lib.ast.Term.Old;
import org.contract_lib.lang.contract_lib.ast.Term.QuantorBinding;
import org.contract_lib.lang.contract_lib.ast.Term.SpecConstant;
import org.contract_lib.lang.contract_lib.ast.Term.Attributes;
import org.contract_lib.lang.contract_lib.ast.Term.BooleanLiteral;
import org.contract_lib.lang.contract_lib.ast.Term.Identifier.IdentifierAs;
import org.contract_lib.lang.contract_lib.ast.Term.Identifier.IdentifierValue;
import org.contract_lib.lang.contract_lib.ast.Term.LetBinding;
import org.contract_lib.lang.contract_lib.ast.Term.MatchBinding;
import org.contract_lib.lang.contract_lib.ast.Term.MethodApplication;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstTranslatorExtension;
import org.contract_lib.lang.contract_lib.label.Identifier;
import org.contract_lib.lang.contract_lib.label.IdentifierMode;
import org.contract_lib.lang.contract_lib.label.IdentifierScope;
import org.contract_lib.lang.contract_lib.label.IdentifierType;
import org.contract_lib.lang.contract_lib.label.LabelStore;
import org.contract_lib.lang.contract_lib.label.IdentifierScope.Total;

/// Shared superclass for all identifier extractors.
public class IdentifierExtractor<M extends IdentifierMode, I extends IdentifierScope, T extends IdentifierType>
    implements ContractLibAstTranslatorExtension {

  private final LabelStore<Identifier<M, I, T>> store;

  public IdentifierExtractor() {
    this.store = new LabelStore<>();
  }

  public Optional<Identifier<M, I, T>> getAddedIdentifer(ContractLibAstElement element) {
    return Optional.ofNullable(
        this.store.getLabel(element));
  }

  public Identifier<M, Total, T> allIdentifer() {
    Set<String> ids = store.getEntries().stream()
        .map(Entry::getValue)
        .map(Identifier::identifier)
        .flatMap(Set::stream)
        .collect(Collectors.toSet());
    return new Identifier<>(ids);
  }

  final void addToStore(ContractLibAstElement element, Identifier<M, I, T> addedIdentifier) {
    store.putLabel(element, addedIdentifier);
  }

  // AST Extension

  @Override
  public void extensionContractLibAst(ContractLibAst res, ContractLIBParser.Start_Context ctx) {
  }

  // Command Exstensions

  @Override
  public void extensionAssert(Assert res, ContractLIBParser.Cmd_assertContext ctx) {
  }

  // - Declare Extensions

  @Override
  public void extensionCmdDeclareAbstraction(
      Abstraction res,
      ContractLIBParser.Cmd_declareAbstractionContext ctx) {
  }

  @Override
  public void extensionCmdDeclareAbstractions(
      JoinedCommand<Abstraction> res,
      ContractLIBParser.Cmd_declareAbstractionsContext ctx) {
  }

  @Override
  public void extensionCmdDeclareConstant(
      Constant res,
      ContractLIBParser.Cmd_declareConstContext ctx) {
  }

  public void extensionCmdDeclareDatatype(
      Datatype res,
      ContractLIBParser.Cmd_declareDatatypeContext ctx) {
  }

  @Override
  public void extensionCmdDeclareDatatypes(
      JoinedCommand<Datatype> res,
      ContractLIBParser.Cmd_declareDatatypesContext ctx) {
  }

  @Override
  public void extensionCmdDeclareFun(
      FunctionDec res,
      ContractLIBParser.Cmd_declareFunContext ctx) {
  }

  @Override
  public void extensionCmdDeclareSort(
      SortDec res,
      ContractLIBParser.Cmd_declareSortContext ctx) {
  }

  // - Define Extensions

  @Override
  public void extensionCmdDefineFun(
      FunctionDec res,
      ContractLIBParser.Cmd_defineFunContext ctx) {

  }

  @Override
  public void extensionCmdDefineFunRec(
      FunctionDec res,
      ContractLIBParser.Cmd_defineFunRecContext ctx) {
  }

  @Override
  public void extensionCmdDefineFunsRec(
      JoinedCommand<FunctionDec> res,
      ContractLIBParser.Cmd_defineFunsRecContext ctx) {
  }

  @Override
  public void extensionCmdDefineSort(
      SortDec res,
      ContractLIBParser.Cmd_defineSortContext ctx) {
  }

  @Override
  public void extensionCmdDefineContract(
      Contract res,
      ContractLIBParser.Cmd_defineContractContext ctx) {
  }

  // Sort Dec Extensions

  @Override
  public void extensionSortDec(
      SortDec res,
      ContractLIBParser.Sort_decContext ctx) {
  }

  @Override
  public void extensionConstructor(
      Constructor res,
      ContractLIBParser.Constructor_decContext ctx) {
  }

  @Override
  public void extensionSelector(
      SelectorDec res,
      ContractLIBParser.Selector_decContext ctx) {
  }

  // Sort Extensions

  @Override
  public void extensionSortParametricType(
      ParametricType res,
      ContractLIBParser.SortContext ctx) {
  }

  @Override
  public void extensionSortType(
      Type res,
      ContractLIBParser.SortContext ctx) {
  }

  // Contract Exstensions

  @Override
  public void extensionFormal(
      Formal res,
      ContractLIBParser.FormalContext ctx) {
  }

  @Override
  public void extensionPrePostPair(
      PrePostPair res,
      ContractLIBParser.ContractContext ctx) {
  }

  // Term Extensions

  @Override
  public void extendsionTermSpecConstant(
      SpecConstant res,
      ContractLIBParser.Spec_constantContext ctx) {
  }

  @Override
  public void extensionTermMethodApplication(
      MethodApplication res,
      ContractLIBParser.TermContext ctx) {
  }

  @Override
  public void extendsionTermNumberLiteral(
      NumberLiteral res,
      ContractLIBParser.Spec_constantContext ctx) {
  }

  @Override
  public void extendsionTermBooleanLiteral(
      BooleanLiteral res,
      ContractLIBParser.TermContext ctx) {
  }

  @Override
  public void extendsionTermIdentifierValue(
      IdentifierValue res,
      ContractLIBParser.IdentifierContext ctx) {
  }

  @Override
  public void extendsionTermIdentifierAs(
      IdentifierAs res,
      ContractLIBParser.Qual_identiferContext ctx) {
  }

  @Override
  public void extensionTermLetBinding(
      LetBinding res,
      ContractLIBParser.TermContext ctx) {
  }

  @Override
  public void extensionTermOld(
      Old res,
      ContractLIBParser.TermContext ctx) {
  }

  @Override
  public void extensionTermQuantorBinding(
      QuantorBinding res,
      ContractLIBParser.TermContext ctx) {

  }

  @Override
  public void extensionTermMatchBinding(
      MatchBinding res,
      ContractLIBParser.TermContext ctx) {

  }

  @Override
  public void extensionTermAttributes(
      Attributes res,
      ContractLIBParser.TermContext ctx) {

  }

  // - Special Term Components

  @Override
  public void extensionMatchCase(
      MatchCase res,
      ContractLIBParser.Match_caseContext ctx) {
  }

  @Override
  public void extensionPattern(
      Pattern res,
      ContractLIBParser.PatternContext ctx) {
  }

  @Override
  public void extensionVarBinding(
      VarBinding res,
      ContractLIBParser.Var_bindingContext ctx) {
  }

  @Override
  public void extensionSortedVar(
      SortedVar res,
      ContractLIBParser.Sorted_varContext ctx) {
  }

  @Override
  public void extensionNumeral(
      Numeral res,
      ContractLIBParser.NumeralContext ctx) {
  }

  @Override
  public void extendsionAttribute(
      String res,
      ContractLIBParser.AttributeContext ctx) {
  }

  @Deprecated
  // Will be removed and replaced by String
  public void extendsionSymbol(
      String res,
      ContractLIBParser.AttributeContext ctx) {
  }

}
