package org.contract_lib.lang.contract_lib.translator_extensions;

import java.util.List;
import java.util.Optional;

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
import org.contract_lib.lang.contract_lib.ast.Sort.ParametricType;
import org.contract_lib.lang.contract_lib.ast.Sort.Type;
import org.contract_lib.lang.contract_lib.ast.SortDec;
import org.contract_lib.lang.contract_lib.ast.SortedVar;
import org.contract_lib.lang.contract_lib.ast.Term.Attributes;
import org.contract_lib.lang.contract_lib.ast.Term.BooleanLiteral;
import org.contract_lib.lang.contract_lib.ast.Term.Identifier.IdentifierAs;
import org.contract_lib.lang.contract_lib.ast.Term.Identifier.IdentifierValue;
import org.contract_lib.lang.contract_lib.ast.Term.LetBinding;
import org.contract_lib.lang.contract_lib.ast.Term.MatchBinding;
import org.contract_lib.lang.contract_lib.ast.Term.MethodApplication;
import org.contract_lib.lang.contract_lib.ast.Term.NumberLiteral;
import org.contract_lib.lang.contract_lib.ast.Term.Old;
import org.contract_lib.lang.contract_lib.ast.Term.QuantorBinding;
import org.contract_lib.lang.contract_lib.ast.Term.SpecConstant;
import org.contract_lib.lang.contract_lib.ast.VarBinding;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstTranslatorExtension;
import org.contract_lib.lang.contract_lib.label.LabelStore;

public final class ParentLinker implements ContractLibAstTranslatorExtension {

  @FunctionalInterface
  private static interface FieldAccess {
    ContractLibAstElement getField();
  }

  @FunctionalInterface
  private static interface ListFieldAccess<E extends ContractLibAstElement> {
    List<? extends ContractLibAstElement> getField();
  }

  private final LabelStore<ContractLibAstElement> store;

  public ParentLinker() {
    this.store = new LabelStore<>();
  }

  public Optional<ContractLibAstElement> getParent(ContractLibAstElement element) {
    return Optional.ofNullable(
        this.store.getLabel(element));
  }

  @Override
  public void extensionContractLibAst(ContractLibAst res, ContractLIBParser.Start_Context ctx) {
    addParentList(
        res,
        List.of(
            res::datatypes,
            res::abstractions,
            res::sorts,
            res::sortParameter,
            res::functions,
            res::constants,
            res::contracts,
            res::asserts));
  }

  @Override
  public void extensionAssert(Assert res, ContractLIBParser.Cmd_assertContext ctx) {
    addParent(res, res::term);
  }

  @Override
  public void extensionCmdDeclareAbstraction(
      Abstraction res,
      ContractLIBParser.Cmd_declareAbstractionContext ctx) {
    addParent(res,
        List.of(res::identifier, res::datatypeDec));
  }

  @Override
  public void extensionCmdDeclareAbstractions(
      JoinedCommand<Abstraction> res,
      ContractLIBParser.Cmd_declareAbstractionsContext ctx) {
    addParentList(res, List.of(res::commands));
  }

  @Override
  public void extensionCmdDeclareConstant(
      Constant res,
      ContractLIBParser.Cmd_declareConstContext ctx) {
    addParent(res, List.of(res::symbol, res::sort));
  }

  public void extensionCmdDeclareDatatype(
      Datatype res,
      ContractLIBParser.Cmd_declareDatatypeContext ctx) {
    addParent(res, List.of(res::identifier, res::dtDec));
  }

  // AST Extension

  @Override
  public void extensionCmdDeclareDatatypes(
      JoinedCommand<Datatype> res,
      ContractLIBParser.Cmd_declareDatatypesContext ctx) {
    addParentList(res, List.of(res::commands));
  }

  // Command Exstensions

  @Override
  public void extensionCmdDeclareFun(
      FunctionDec res,
      ContractLIBParser.Cmd_declareFunContext ctx) {
    //TODO
  }

  // - Declare Extensions

  @Override
  public void extensionCmdDeclareSort(
      SortDec res,
      ContractLIBParser.Cmd_declareSortContext ctx) {
    addParent(res, List.of(res::name, res::rank));
  }

  @Override
  public void extensionCmdDefineFun(
      FunctionDec res,
      ContractLIBParser.Cmd_defineFunContext ctx) {
    //TODO
  }

  @Override
  public void extensionCmdDefineFunRec(
      FunctionDec res,
      ContractLIBParser.Cmd_defineFunRecContext ctx) {
    //TODO
  }

  @Override
  public void extensionCmdDefineFunsRec(
      JoinedCommand<FunctionDec> res,
      ContractLIBParser.Cmd_defineFunsRecContext ctx) {
    addParentList(res, List.of(res::commands));
  }

  @Override
  public void extensionCmdDefineSort(
      SortDec res,
      ContractLIBParser.Cmd_defineSortContext ctx) {
    //TODO
  }

  @Override
  public void extensionCmdDefineContract(
      Contract res,
      ContractLIBParser.Cmd_defineContractContext ctx) {
    addParent(
        res,
        List.of(res::identifier),
        List.of(res::formals, res::pairs));
  }

  @Override
  public void extensionSortDec(
      SortDec res,
      ContractLIBParser.Sort_decContext ctx) {
    addParent(res, List.of(res::name, res::rank));
  }

  // - Define Extensions

  @Override
  public void extensionConstructor(
      Constructor res,
      ContractLIBParser.Constructor_decContext ctx) {
    //TODO
  }

  @Override
  public void extensionSelector(
      SelectorDec res,
      ContractLIBParser.Selector_decContext ctx) {
    //TODO
  }

  @Override
  public void extensionSortParametricType(
      ParametricType res,
      ContractLIBParser.SortContext ctx) {
    //TODO
  }

  @Override
  public void extensionSortType(
      Type res,
      ContractLIBParser.SortContext ctx) {
    //TODO
  }

  @Override
  public void extensionFormal(
      Formal res,
      ContractLIBParser.FormalContext ctx) {
    //TODO
  }

  // Sort Dec Extensions

  @Override
  public void extensionPrePostPair(
      PrePostPair res,
      ContractLIBParser.ContractContext ctx) {
    //TODO
  }

  @Override
  public void extendsionTermSpecConstant(
      SpecConstant res,
      ContractLIBParser.Spec_constantContext ctx) {
    //TODO
  }

  @Override
  public void extensionTermMethodApplication(
      MethodApplication res,
      ContractLIBParser.TermContext ctx) {
    //TODO
  }

  // Sort Extensions

  @Override
  public void extendsionTermNumberLiteral(
      NumberLiteral res,
      ContractLIBParser.Spec_constantContext ctx) {
    //TODO
  }

  @Override
  public void extendsionTermBooleanLiteral(
      BooleanLiteral res,
      ContractLIBParser.TermContext ctx) {
    //TODO
  }

  // Contract Exstensions

  @Override
  public void extendsionTermIdentifierValue(
      IdentifierValue res,
      ContractLIBParser.IdentifierContext ctx) {
    //TODO
  }

  @Override
  public void extendsionTermIdentifierAs(
      IdentifierAs res,
      ContractLIBParser.Qual_identiferContext ctx) {
    //TODO
  }

  // Term Extensions

  @Override
  public void extensionTermLetBinding(
      LetBinding res,
      ContractLIBParser.TermContext ctx) {
    //TODO
  }

  @Override
  public void extensionTermOld(
      Old res,
      ContractLIBParser.TermContext ctx) {
    //TODO
  }

  @Override
  public void extensionTermQuantorBinding(
      QuantorBinding res,
      ContractLIBParser.TermContext ctx) {
    //TODO
  }

  @Override
  public void extensionTermMatchBinding(
      MatchBinding res,
      ContractLIBParser.TermContext ctx) {
    //TODO
  }

  @Override
  public void extensionTermAttributes(
      Attributes res,
      ContractLIBParser.TermContext ctx) {
    //TODO
  }

  @Override
  public void extensionMatchCase(
      MatchCase res,
      ContractLIBParser.Match_caseContext ctx) {
    //TODO
  }

  @Override
  public void extensionPattern(
      Pattern res,
      ContractLIBParser.PatternContext ctx) {
    //TODO
  }

  @Override
  public void extensionVarBinding(
      VarBinding res,
      ContractLIBParser.Var_bindingContext ctx) {
    //TODO
  }

  @Override
  public void extensionSortedVar(
      SortedVar res,
      ContractLIBParser.Sorted_varContext ctx) {
    //TODO
  }

  @Override
  public void extensionNumeral(
      Numeral res,
      ContractLIBParser.NumeralContext ctx) {
    //TODO
  }

  @Override
  public void extendsionAttribute(
      String res,
      ContractLIBParser.AttributeContext ctx) {
    // TODO: Cannot work with Strings
  }

  // - Special Term Components

  @Deprecated
  // Will be removed and replaced by String
  public void extendsionSymbol(
      String res,
      ContractLIBParser.AttributeContext ctx) {
    // TODO: Deprecated, to be removed
  }

  private final <E extends ContractLibAstElement> void addParent(
      E parent, ContractLibAstElement field) {
    //TODO: Check that label does not exist
    store.putLabel(field, parent);
  }

  private final <E extends ContractLibAstElement> void addParent(
      E parent, FieldAccess fieldAccess) {
    addParent(parent, fieldAccess.getField());
  }

  private final <E extends ContractLibAstElement> void addParent(
      E parent, ListFieldAccess<E> listFieldsAccess) {
    listFieldsAccess.getField().stream()
        .forEach(f -> addParent(parent, f));
  }

  private final <E extends ContractLibAstElement> void addParent(
      E parent, List<FieldAccess> listFieldsAccess) {
    listFieldsAccess
        .stream()
        .forEach(f -> addParent(parent, f));
  }

  private final <E extends ContractLibAstElement> void addParentList(
      E parent, List<ListFieldAccess<E>> toListOfListFieldAccess) {
    toListOfListFieldAccess.stream()
        .forEach(l -> addParent(parent, l));
  }

  private final <E extends ContractLibAstElement> void addParent(
      E parent,
      List<FieldAccess> toFields,
      List<ListFieldAccess<E>> toListFields) {
    addParent(parent, toFields);
    addParentList(parent, toListFields);
  }

}
