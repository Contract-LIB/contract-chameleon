package org.contract_lib.lang.contract_lib.translator_extensions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser;
import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser.Datatype_decContext;
import org.contract_lib.lang.contract_lib.ast.Abstraction;
import org.contract_lib.lang.contract_lib.ast.Assert;
import org.contract_lib.lang.contract_lib.ast.Command;
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

/// Creates a list in what order the commands appear in the parse tree.
public final class CommandOrderExtractor implements ContractLibAstTranslatorExtension {

  private final Map<Command, Integer> index;
  private final List<Command> commands;
  private Integer next = 0;

  public CommandOrderExtractor() {
    this.index = new IdentityHashMap<>();
    this.commands = new ArrayList<>();
  }

  public Optional<Command> predecessor(Command command) {
    return Optional.ofNullable(index.get(command))
        .map(this::dec)
        .flatMap(this::testRange)
        .map(commands::get);
  }

  public Optional<Command> successor(Command command) {
    return Optional.ofNullable(index.get(command))
        .map(this::inc)
        .flatMap(this::testRange)
        .map(commands::get);
  }

  public Stream<Command> getCommands() {
    return commands.stream();
  }

  @Override
  public String toString() {
    return String.format("Command Order [%n%s%n]",
        commands
            .stream()
            .map((e) -> String.format("  %s", e))
            .collect(Collectors.joining(System.lineSeparator())));
  }

  private void add(Command res) {
    this.index.put(res, next++);
    this.commands.add(res);
  }

  private Optional<Integer> testRange(Integer i) {
    return i >= commands.size() || i < 0 ? Optional.empty() : Optional.of(i);
  }

  private Integer inc(Integer i) {
    return i + 1;
  }

  private Integer dec(Integer i) {
    return i - 1;
  }

  // AST Extension

  @Override
  public void extensionContractLibAst(ContractLibAst res, ContractLIBParser.Start_Context ctx) {
  }

  // Command Exstensions

  @Override
  public void extensionAssert(Assert res, ContractLIBParser.Cmd_assertContext ctx) {
    this.add(res);
  }

  // - Declare Extensions

  @Override
  public void extensionCmdDeclareAbstraction(
      Abstraction res,
      ContractLIBParser.Cmd_declareAbstractionContext ctx) {
    this.add(res);
  }

  @Override
  public void extensionCmdDeclareAbstractions(
      JoinedCommand<Abstraction> res,
      ContractLIBParser.Cmd_declareAbstractionsContext ctx) {
    this.add(res);
  }

  @Override
  public void extensionCmdDeclareConstant(
      Constant res,
      ContractLIBParser.Cmd_declareConstContext ctx) {
    this.add(res);
  }

  public void extensionCmdDeclareDatatype(
      Datatype res,
      ContractLIBParser.Cmd_declareDatatypeContext ctx) {
    this.add(res);
  }

  @Override
  public void extensionCmdDeclareDatatypes(
      JoinedCommand<Datatype> res,
      ContractLIBParser.Cmd_declareDatatypesContext ctx) {
    this.add(res);
  }

  @Override
  public void extensionCmdDeclareFun(
      FunctionDec res,
      ContractLIBParser.Cmd_declareFunContext ctx) {
    this.add(res);
  }

  @Override
  public void extensionCmdDeclareSort(
      SortDec res,
      ContractLIBParser.Cmd_declareSortContext ctx) {
    this.add(res);
  }

  // - Define Extensions

  @Override
  public void extensionCmdDefineFun(
      FunctionDec res,
      ContractLIBParser.Cmd_defineFunContext ctx) {
    this.add(res);
  }

  @Override
  public void extensionCmdDefineFunRec(
      FunctionDec res,
      ContractLIBParser.Cmd_defineFunRecContext ctx) {
    this.add(res);
  }

  @Override
  public void extensionCmdDefineFunsRec(
      JoinedCommand<FunctionDec> res,
      ContractLIBParser.Cmd_defineFunsRecContext ctx) {
    this.add(res);
  }

  @Override
  public void extensionCmdDefineSort(
      SortDec res,
      ContractLIBParser.Cmd_defineSortContext ctx) {
    this.add(res);
  }

  @Override
  public void extensionCmdDefineContract(
      Contract res,
      ContractLIBParser.Cmd_defineContractContext ctx) {
    this.add(res);
  }

  // Sort Dec Extensions

  @Override
  public void extensionDatatypeDec(
      DatatypeDec res,
      Datatype_decContext ctx) {
  }

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
