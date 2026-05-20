package org.contract_lib.lang.contract_lib.generator;

import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser;
import org.contract_lib.lang.contract_lib.ast.Abstraction;
import org.contract_lib.lang.contract_lib.ast.Assert;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.ast.Term;

public abstract class ContractLibAstTranslatorExtension {
  public abstract void extendsionContractLibAst(ContractLibAst res, ContractLIBParser.Start_Context ctx);

  public abstract void extendsionAssert(Assert res, ContractLIBParser.Cmd_assertContext ctx);

  public abstract void extendsionAbstraction(Abstraction res, ContractLIBParser.Cmd_declareAbstractionContext ctx);

  public abstract void extendsionTerm(Term res, ContractLIBParser.TermContext ctx);
  //TODO: To extend
}
