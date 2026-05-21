package org.contract_lib.lang.contract_lib.translator_extensions;

import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.antlr.v4.runtime.ParserRuleContext;
import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser;
import org.contract_lib.lang.contract_lib.ast.Abstraction;
import org.contract_lib.lang.contract_lib.ast.Assert;
import org.contract_lib.lang.contract_lib.ast.Constant;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;
import org.contract_lib.lang.contract_lib.ast.Datatype;
import org.contract_lib.lang.contract_lib.ast.FunctionDec;
import org.contract_lib.lang.contract_lib.ast.SortDec;
import org.contract_lib.lang.contract_lib.ast.Term;
import org.contract_lib.lang.contract_lib.generator.ContractLibAstTranslatorExtension;
import org.contract_lib.lang.contract_lib.label.FilePoint;
import org.contract_lib.lang.contract_lib.label.FilePosition;
import org.contract_lib.lang.contract_lib.label.LabelStore;

public final class FilePositionExtractor extends ContractLibAstTranslatorExtension {

  private final LabelStore<FilePosition> store;

  public FilePositionExtractor() {
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

  public void extendsionContractLibAst(ContractLibAst res, ContractLIBParser.Start_Context ctx) {
    addToStore(res, ctx);
  }

  public void extendsionAssert(Assert res, ContractLIBParser.Cmd_assertContext ctx) {
    addToStore(res, ctx);
  }

  public void extendsionTerm(Term res, ContractLIBParser.TermContext ctx) {
    addToStore(res, ctx);
  }

  public void extendsionDeclareAbstraction(
      Abstraction res,
      ContractLIBParser.Cmd_declareAbstractionContext ctx) {
    addToStore(res, ctx);
  }

  public void extendsionDeclareAbstractions(
      Stream<Abstraction> res,
      ContractLIBParser.Cmd_declareAbstractionsContext ctx) {
    //TODO: think about the two ranges that apply here (one for the sort def, and one for the constructor)?
    //addToStore(res, ctx);
  }

  public void extendsionDeclareConstant(
      Constant res,
      ContractLIBParser.Cmd_declareConstContext ctx) {

    addToStore(res, ctx);
  }

  public void extendsionDeclareDatatype(
      Datatype res,
      ContractLIBParser.Cmd_declareDatatypeContext ctx) {
    addToStore(res, ctx);
  }

  public void extendsionDeclareDatatypes(
      Stream<Datatype> res,
      ContractLIBParser.Cmd_declareDatatypesContext ctx) {
    //TODO: think about the two ranges that apply here (one for the sort def, and one for the constructor)?
    //addToStore(res, ctx);
  }

  public void extendsionDeclareFun(
      FunctionDec res,
      ContractLIBParser.Cmd_declareFunContext ctx) {
    addToStore(res, ctx);
  }

  public void extendsionDeclareSort(
      SortDec.Def res,
      ContractLIBParser.Cmd_declareSortContext ctx) {
    addToStore(res, ctx);
  }

}
