package org.contract_lib.lang.contract_lib.generator;

import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser;
import org.contract_lib.lang.contract_lib.ast.Abstraction;
import org.contract_lib.lang.contract_lib.ast.Assert;
import org.contract_lib.lang.contract_lib.ast.Constant;
import org.contract_lib.lang.contract_lib.ast.Constructor;
import org.contract_lib.lang.contract_lib.ast.Contract;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.ast.Datatype;
import org.contract_lib.lang.contract_lib.ast.DatatypeDec;
import org.contract_lib.lang.contract_lib.ast.Formal;
import org.contract_lib.lang.contract_lib.ast.FunctionDec;
import org.contract_lib.lang.contract_lib.ast.JoinedCommand;
import org.contract_lib.lang.contract_lib.ast.MatchCase;
import org.contract_lib.lang.contract_lib.ast.Numeral;
import org.contract_lib.lang.contract_lib.ast.Pattern;
import org.contract_lib.lang.contract_lib.ast.PrePostPair;
import org.contract_lib.lang.contract_lib.ast.SelectorDec;
import org.contract_lib.lang.contract_lib.ast.Sort;
import org.contract_lib.lang.contract_lib.ast.SortDec;
import org.contract_lib.lang.contract_lib.ast.SortedVar;
import org.contract_lib.lang.contract_lib.ast.VarBinding;
import org.contract_lib.lang.contract_lib.ast.Term.Attributes;
import org.contract_lib.lang.contract_lib.ast.Term.BooleanLiteral;
import org.contract_lib.lang.contract_lib.ast.Term.LetBinding;
import org.contract_lib.lang.contract_lib.ast.Term.MatchBinding;
import org.contract_lib.lang.contract_lib.ast.Term.MethodApplication;
import org.contract_lib.lang.contract_lib.ast.Term.NumberLiteral;
import org.contract_lib.lang.contract_lib.ast.Term.QuantorBinding;
import org.contract_lib.lang.contract_lib.ast.Term.SpecConstant;
import org.contract_lib.lang.contract_lib.ast.Term.Old;
import org.contract_lib.lang.contract_lib.ast.Term.Identifier.IdentifierAs;
import org.contract_lib.lang.contract_lib.ast.Term.Identifier.IdentifierValue;

/**
 * The idea of the translator extension are a flexible approach to allow labeling the
 * Contract-LIB AST, while keeping the AST itself minimal.
 *
 * This will allow to add fields to the AST nodes without the need to redefine those nodes.
 * Moreover, it allows the Contract-LIB AST to abstract from the parse tree,
 * while extensions can link back to the parse nodes, where required (e.g. to get the origin in the file).
 */
public interface ContractLibAstTranslatorExtension {

  // AST Extension

  public void extensionContractLibAst(ContractLibAst res, ContractLIBParser.Start_Context ctx);

  // Command Exstensions

  public void extensionAssert(Assert res, ContractLIBParser.Cmd_assertContext ctx);

  // - Declare Extensions

  public void extensionCmdDeclareAbstraction(
      Abstraction res,
      ContractLIBParser.Cmd_declareAbstractionContext ctx);

  public void extensionCmdDeclareAbstractions(
      JoinedCommand<Abstraction> res,
      ContractLIBParser.Cmd_declareAbstractionsContext ctx);

  public void extensionCmdDeclareConstant(
      Constant res,
      ContractLIBParser.Cmd_declareConstContext ctx);

  public void extensionCmdDeclareDatatype(
      Datatype res,
      ContractLIBParser.Cmd_declareDatatypeContext ctx);

  public void extensionCmdDeclareDatatypes(
      JoinedCommand<Datatype> res,
      ContractLIBParser.Cmd_declareDatatypesContext ctx);

  public void extensionCmdDeclareFun(
      FunctionDec res,
      ContractLIBParser.Cmd_declareFunContext ctx);

  public void extensionCmdDeclareSort(
      SortDec res,
      ContractLIBParser.Cmd_declareSortContext ctx);

  // - Define Extensions

  public void extensionCmdDefineFun(
      FunctionDec res,
      ContractLIBParser.Cmd_defineFunContext ctx);

  public void extensionCmdDefineFunRec(
      FunctionDec res,
      ContractLIBParser.Cmd_defineFunRecContext ctx);

  public void extensionCmdDefineFunsRec(
      JoinedCommand<FunctionDec> res,
      ContractLIBParser.Cmd_defineFunsRecContext ctx);

  public void extensionCmdDefineSort(
      SortDec res,
      ContractLIBParser.Cmd_defineSortContext ctx);

  public void extensionCmdDefineContract(
      Contract res,
      ContractLIBParser.Cmd_defineContractContext ctx);

  // Sort Dec Extensions

  public void extensionDatatypeDec(
      DatatypeDec res,
      ContractLIBParser.Datatype_decContext ctx);

  public void extensionSortDec(
      SortDec res,
      ContractLIBParser.Sort_decContext ctx);

  public void extensionConstructor(
      Constructor res,
      ContractLIBParser.Constructor_decContext ctx);

  public void extensionSelector(
      SelectorDec res,
      ContractLIBParser.Selector_decContext ctx);

  // Sort Extensions

  public void extensionSortParametricType(
      Sort.ParametricType res,
      ContractLIBParser.SortContext ctx);

  public void extensionSortType(
      Sort.Type res,
      ContractLIBParser.SortContext ctx);

  // Contract Exstensions

  public void extensionFormal(
      Formal res,
      ContractLIBParser.FormalContext ctx);

  public void extensionPrePostPair(
      PrePostPair res,
      ContractLIBParser.ContractContext ctx);

  // Term Extensions

  public void extendsionTermSpecConstant(
      SpecConstant res,
      ContractLIBParser.Spec_constantContext ctx);

  public void extensionTermMethodApplication(
      MethodApplication res,
      ContractLIBParser.TermContext ctx);

  public void extendsionTermNumberLiteral(
      NumberLiteral res,
      ContractLIBParser.Spec_constantContext ctx);

  public void extendsionTermBooleanLiteral(
      BooleanLiteral res,
      ContractLIBParser.TermContext ctx);

  public void extendsionTermIdentifierValue(
      IdentifierValue res,
      ContractLIBParser.IdentifierContext ctx);

  public void extendsionTermIdentifierAs(
      IdentifierAs res,
      ContractLIBParser.Qual_identiferContext ctx);

  public void extensionTermLetBinding(
      LetBinding res,
      ContractLIBParser.TermContext ctx);

  public void extensionTermOld(
      Old res,
      ContractLIBParser.TermContext ctx);

  public void extensionTermQuantorBinding(
      QuantorBinding res,
      ContractLIBParser.TermContext ctx);

  public void extensionTermMatchBinding(
      MatchBinding res,
      ContractLIBParser.TermContext ctx);

  public void extensionTermAttributes(
      Attributes res,
      ContractLIBParser.TermContext ctx);

  // - Special Term Components

  public void extensionMatchCase(
      MatchCase res,
      ContractLIBParser.Match_caseContext ctx);

  public void extensionPattern(
      Pattern res,
      ContractLIBParser.PatternContext ctx);

  public void extensionVarBinding(
      VarBinding res,
      ContractLIBParser.Var_bindingContext ctx);

  public void extensionSortedVar(
      SortedVar res,
      ContractLIBParser.Sorted_varContext ctx);

  public void extensionNumeral(
      Numeral res,
      ContractLIBParser.NumeralContext ctx);

  public void extendsionAttribute(
      String res,
      ContractLIBParser.AttributeContext ctx);

  @Deprecated
  // Will be removed and replaced by String
  public void extendsionSymbol(
      String res,
      ContractLIBParser.AttributeContext ctx);
}
