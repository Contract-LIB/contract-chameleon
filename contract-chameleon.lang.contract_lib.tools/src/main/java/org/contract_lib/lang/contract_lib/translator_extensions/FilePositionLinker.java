package org.contract_lib.lang.contract_lib.translator_extensions;

import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;

import org.antlr.v4.runtime.ParserRuleContext;
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
import org.contract_lib.lang.contract_lib.label.FilePoint;
import org.contract_lib.lang.contract_lib.label.FilePosition;
import org.contract_lib.lang.contract_lib.label.LabelStore;

public final class FilePositionLinker implements ContractLibAstTranslatorExtension {

  private final LabelStore<FilePosition> store;

  public FilePositionLinker() {
    this.store = new LabelStore<>();
  }

  public Optional<FilePosition> getFilePosition(ContractLibAstElement element) {
    return Optional.ofNullable(
        this.store.getLabel(element));
  }

  public Set<Entry<ContractLibAstElement, FilePosition>> getEntries() {
    return store.getEntries();
  }

  private final void addToStore(ContractLibAstElement element, ParserRuleContext ctx) {

    FilePoint start = new FilePoint(
        ctx.start.getLine(),
        ctx.start.getCharPositionInLine());

    FilePoint end = new FilePoint(
        ctx.stop.getLine(),
        ctx.stop.getCharPositionInLine() + ctx.stop.getStopIndex() - ctx.stop.getStartIndex());

    store.putLabel(element, new FilePosition(start, end));
  }

  // AST Extension

  @Override
  public void extensionContractLibAst(ContractLibAst res, ContractLIBParser.Start_Context ctx) {
    addToStore(res, ctx);
  }

  // Command Exstensions

  @Override
  public void extensionAssert(Assert res, ContractLIBParser.Cmd_assertContext ctx) {
    addToStore(res, ctx);
  }

  // - Declare Extensions

  @Override
  public void extensionCmdDeclareAbstraction(
      Abstraction res,
      ContractLIBParser.Cmd_declareAbstractionContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extensionCmdDeclareAbstractions(
      JoinedCommand<Abstraction> res,
      ContractLIBParser.Cmd_declareAbstractionsContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extensionCmdDeclareConstant(
      Constant res,
      ContractLIBParser.Cmd_declareConstContext ctx) {

    addToStore(res, ctx);
  }

  public void extensionCmdDeclareDatatype(
      Datatype res,
      ContractLIBParser.Cmd_declareDatatypeContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extensionCmdDeclareDatatypes(
      JoinedCommand<Datatype> res,
      ContractLIBParser.Cmd_declareDatatypesContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extensionCmdDeclareFun(
      FunctionDec res,
      ContractLIBParser.Cmd_declareFunContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extensionCmdDeclareSort(
      SortDec res,
      ContractLIBParser.Cmd_declareSortContext ctx) {
    addToStore(res, ctx);
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
    addToStore(res, ctx);
  }

  @Override
  public void extensionCmdDefineFunsRec(
      JoinedCommand<FunctionDec> res,
      ContractLIBParser.Cmd_defineFunsRecContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extensionCmdDefineSort(
      SortDec res,
      ContractLIBParser.Cmd_defineSortContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extensionCmdDefineContract(
      Contract res,
      ContractLIBParser.Cmd_defineContractContext ctx) {
    addToStore(res, ctx);
  }

  // Sort Dec Extensions

  @Override
  public void extensionSortDec(
      SortDec res,
      ContractLIBParser.Sort_decContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extensionConstructor(
      Constructor res,
      ContractLIBParser.Constructor_decContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extensionSelector(
      SelectorDec res,
      ContractLIBParser.Selector_decContext ctx) {
    addToStore(res, ctx);
  }

  // Sort Extensions

  @Override
  public void extensionSortParametricType(
      ParametricType res,
      ContractLIBParser.SortContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extensionSortType(
      Type res,
      ContractLIBParser.SortContext ctx) {
    addToStore(res, ctx);
  }

  // Contract Exstensions

  @Override
  public void extensionFormal(
      Formal res,
      ContractLIBParser.FormalContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extensionPrePostPair(
      PrePostPair res,
      ContractLIBParser.ContractContext ctx) {
    addToStore(res, ctx);
  }

  // Term Extensions

  @Override
  public void extendsionTermSpecConstant(
      SpecConstant res,
      ContractLIBParser.Spec_constantContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extensionTermMethodApplication(
      MethodApplication res,
      ContractLIBParser.TermContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extendsionTermNumberLiteral(
      NumberLiteral res,
      ContractLIBParser.Spec_constantContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extendsionTermBooleanLiteral(
      BooleanLiteral res,
      ContractLIBParser.TermContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extendsionTermIdentifierValue(
      IdentifierValue res,
      ContractLIBParser.IdentifierContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extendsionTermIdentifierAs(
      IdentifierAs res,
      ContractLIBParser.Qual_identiferContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extensionTermLetBinding(
      LetBinding res,
      ContractLIBParser.TermContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extensionTermOld(
      Old res,
      ContractLIBParser.TermContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extensionTermQuantorBinding(
      QuantorBinding res,
      ContractLIBParser.TermContext ctx) {

    addToStore(res, ctx);
  }

  @Override
  public void extensionTermMatchBinding(
      MatchBinding res,
      ContractLIBParser.TermContext ctx) {

    addToStore(res, ctx);
  }

  @Override
  public void extensionTermAttributes(
      Attributes res,
      ContractLIBParser.TermContext ctx) {

    addToStore(res, ctx);
  }

  // - Special Term Components

  @Override
  public void extensionMatchCase(
      MatchCase res,
      ContractLIBParser.Match_caseContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extensionPattern(
      Pattern res,
      ContractLIBParser.PatternContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extensionVarBinding(
      VarBinding res,
      ContractLIBParser.Var_bindingContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extensionSortedVar(
      SortedVar res,
      ContractLIBParser.Sorted_varContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extensionNumeral(
      Numeral res,
      ContractLIBParser.NumeralContext ctx) {
    addToStore(res, ctx);
  }

  @Override
  public void extendsionAttribute(
      String res,
      ContractLIBParser.AttributeContext ctx) {
    // TODO: Cannot work with Strings
  }

  @Deprecated
  // Will be removed and replaced by String
  public void extendsionSymbol(
      String res,
      ContractLIBParser.AttributeContext ctx) {
    // TODO: Deprecated, to be removed
  }

}
