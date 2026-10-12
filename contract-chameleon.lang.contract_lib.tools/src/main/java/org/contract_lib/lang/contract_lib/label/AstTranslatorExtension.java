
package org.contract_lib.lang.contract_lib.label;

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
import org.contract_lib.lang.contract_lib.ast.Function;
import org.contract_lib.lang.contract_lib.ast.Constant;
import org.contract_lib.lang.contract_lib.ast.Contract;
import org.contract_lib.lang.contract_lib.ast.Term;

public abstract class AstTranslatorExtension {
  public abstract void extendsionContractLibAst(ContractLibAst res, ContractLIBParser.Start_Context ctx);

  public abstract void extendsionAssert(Assert res, ContractLIBParser.Cmd_assertContext ctx);

  public abstract void extendsionAbstraction(Abstraction res, ContractLIBParser.Cmd_declareAbstractionContext ctx);

  public abstract void extendsionTerm(Term res, ContractLIBParser.TermContext ctx);
  // TODO: To extend
}
