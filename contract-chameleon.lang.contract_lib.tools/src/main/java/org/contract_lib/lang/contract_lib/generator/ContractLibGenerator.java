package org.contract_lib.lang.contract_lib.generator;

import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import java.nio.file.Path;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

import org.contract_lib.contract_chameleon.error.ChameleonMessageManager;

import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBLexer;
import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser;

import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;
import org.contract_lib.lang.contract_lib.label.AstElementOrigin;
import org.contract_lib.lang.contract_lib.label.AstTranslatorExtension;

/// The default class to create the `AST` from a '.clib' file.
public class ContractLibGenerator {

  private List<AstTranslatorExtension> extensions;
  private ChameleonMessageManager manager;

  private AstElementOrigin fileOrigin = new AstElementOrigin();

  public ContractLibGenerator(
      ChameleonMessageManager manager) {
    this(
        manager,
        new ArrayList<>());
  }

  public ContractLibGenerator(
      ChameleonMessageManager manager,
      List<AstTranslatorExtension> extensions) {
    this.manager = manager;
    this.extensions = extensions;
    this.extensions.add(fileOrigin);
  }

  public ContractLibAst generateFromPath(Path filepath) throws IOException {
    CharStream charStream = CharStreams.fromPath(filepath);
    return this.generate(filepath.toString(), charStream);
  }

  public ContractLibAst generateFromFile(String filepath) throws IOException {
    CharStream charStream = CharStreams.fromFileName(filepath);
    return this.generate(filepath, charStream);
  }

  public ContractLibAst generate(
      String locationId,
      CharStream charStream) {
    ContractLIBLexer lexer = new ContractLIBLexer(charStream);
    CommonTokenStream tokenStream = new CommonTokenStream(lexer);
    ContractLIBParser parser = new ContractLIBParser(tokenStream);

    ContractLibAstErrorListener errorHandler = new ContractLibAstErrorListener(
        locationId,
        manager);

    parser.addErrorListener(errorHandler);

    ContractLIBParser.Start_Context parseTree = parser.start_();

    ContractLibAstTranslator translator = new ContractLibAstTranslator(this.extensions, this.manager);

    ContractLibAst ast = translator.translateStart(parseTree);

    return ast;
  }

  public int getLine(ContractLibAstElement element) {
    return fileOrigin.getLine(element);
  }

  public int getLinePosition(ContractLibAstElement element) {
    return fileOrigin.getLinePosition(element);
  }
}
