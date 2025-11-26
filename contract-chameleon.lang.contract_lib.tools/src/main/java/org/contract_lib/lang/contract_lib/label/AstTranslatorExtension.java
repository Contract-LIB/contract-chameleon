
package org.contract_lib.lang.contract_lib.label;

import java.util.List;

import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser;

import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;
import org.contract_lib.lang.contract_lib.ast.Abstraction;
import org.contract_lib.lang.contract_lib.ast.Argument;
import org.contract_lib.lang.contract_lib.ast.Assert;
import org.contract_lib.lang.contract_lib.ast.Constant;
import org.contract_lib.lang.contract_lib.ast.Constructor;
import org.contract_lib.lang.contract_lib.ast.Datatype;
import org.contract_lib.lang.contract_lib.ast.Sort;
import org.contract_lib.lang.contract_lib.ast.FunctionDec;
import org.contract_lib.lang.contract_lib.ast.Constant;
import org.contract_lib.lang.contract_lib.ast.Contract;
import org.contract_lib.lang.contract_lib.ast.Term;

public abstract class AstTranslatorExtension {

  public abstract void extensionContractLibAst(ContractLibAst res, ContractLIBParser.Start_Context ctx);

  public abstract void extensionAssert(Assert res, ContractLIBParser.Cmd_assertContext ctx);

  public abstract void extensionDeclareAbstractions(List<Abstraction> res,
      ContractLIBParser.Cmd_declareAbstractionsContext ctx);

  public abstract void extensionTerm(Term res, ContractLIBParser.TermContext ctx);
  //TODO: To extend
}
