
package org.contract_lib.lang.contract_lib.label;

import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser;
import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser.Cmd_declareAbstractionsContext;
import org.contract_lib.lang.contract_lib.ast.Abstraction;
import org.contract_lib.lang.contract_lib.ast.Assert;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;
import org.contract_lib.lang.contract_lib.ast.Term;

public final class AstElementOrigin extends AstTranslatorExtension {

  private record Pos(
      int line,
      int index) {
  }

  private LabelWrapper<Pos> lables = new LabelWrapper<>();

  public int getLine(ContractLibAstElement element) {
    return lables.getLabel(element).line();
  }

  public int getLinePosition(ContractLibAstElement element) {
    return lables.getLabel(element).index();
  }

  @Override
  public void extensionContractLibAst(ContractLibAst res, ContractLIBParser.Start_Context ctx) {
    contextToPos(res, ctx);
  }

  @Override
  public void extensionAssert(Assert res,
      ContractLIBParser.Cmd_assertContext ctx) {
    contextToPos(res, ctx);
  }

  @Override
  public void extensionDeclareAbstractions(List<Abstraction> res, Cmd_declareAbstractionsContext ctx) {
    res.forEach((r) -> contextToPos(r, ctx));
  }

  @Override
  public void extensionTerm(Term res, ContractLIBParser.TermContext ctx) {
    contextToPos(res, ctx);
  }

  private void contextToPos(ContractLibAstElement element, ParserRuleContext ctx) {
    lables.putLabel(element, new Pos(ctx.start.getLine(), ctx.start.getCharPositionInLine()));
  }
}
