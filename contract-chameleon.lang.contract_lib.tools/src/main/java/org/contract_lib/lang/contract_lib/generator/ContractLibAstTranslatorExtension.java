package org.contract_lib.lang.contract_lib.generator;

import java.util.stream.Stream;

import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser;
import org.contract_lib.lang.contract_lib.ast.Abstraction;
import org.contract_lib.lang.contract_lib.ast.Assert;
import org.contract_lib.lang.contract_lib.ast.Constant;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.ast.Datatype;
import org.contract_lib.lang.contract_lib.ast.FunctionDec;
import org.contract_lib.lang.contract_lib.ast.SortDec;
import org.contract_lib.lang.contract_lib.ast.Term;

public abstract class ContractLibAstTranslatorExtension {
  public abstract void extendsionContractLibAst(ContractLibAst res, ContractLIBParser.Start_Context ctx);

  public abstract void extendsionAssert(Assert res, ContractLIBParser.Cmd_assertContext ctx);

  public abstract void extendsionTerm(Term res, ContractLIBParser.TermContext ctx);

  public abstract void extendsionDeclareAbstraction(
      Abstraction res,
      ContractLIBParser.Cmd_declareAbstractionContext ctx);

  public abstract void extendsionDeclareAbstractions(
      Stream<Abstraction> res,
      ContractLIBParser.Cmd_declareAbstractionsContext ctx);

  public abstract void extendsionDeclareConstant(
      Constant res,
      ContractLIBParser.Cmd_declareConstContext ctx);

  public abstract void extendsionDeclareDatatype(
      Datatype res,
      ContractLIBParser.Cmd_declareDatatypeContext ctx);

  public abstract void extendsionDeclareDatatypes(
      Stream<Datatype> res,
      ContractLIBParser.Cmd_declareDatatypesContext ctx);

  public abstract void extendsionDeclareFun(
      FunctionDec res,
      ContractLIBParser.Cmd_declareFunContext ctx);

  public abstract void extendsionDeclareSort(
      SortDec.Def res,
      ContractLIBParser.Cmd_declareSortContext ctx);

  //TODO: To extend
}
