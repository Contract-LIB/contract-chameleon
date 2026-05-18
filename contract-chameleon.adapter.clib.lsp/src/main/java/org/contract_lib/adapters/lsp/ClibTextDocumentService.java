package org.contract_lib.adapters.lsp;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.io.IOException;

import org.eclipse.lsp4j.services.TextDocumentService;

import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.TextDocumentSyncKind;
import org.eclipse.lsp4j.TextDocumentContentChangeEvent;

import org.eclipse.lsp4j.Hover;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.HoverParams;
import org.eclipse.lsp4j.MarkupContent;
import org.eclipse.lsp4j.CompletionParams;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.InsertTextFormat;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.Range;

import org.eclipse.lsp4j.jsonrpc.messages.Either;

import org.contract_lib.contract_chameleon.contexts.MessageContext;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.generator.ContractLibGenerator;

public class ClibTextDocumentService implements TextDocumentService {

  // Maps a file URI (e.g., "file:///project/main.custom") to its parsed AST
  private final ConcurrentHashMap<String, ContractLibAst> astCache = new ConcurrentHashMap<>();
  private final MessageContext messageContext;

  public ClibTextDocumentService(MessageContext messageContext) {
    this.messageContext = messageContext;
  }

  // - File Management

  // 1. Triggered when a file is opened in LazyVim
  @Override
  public void didOpen(DidOpenTextDocumentParams params) {
    String uri = removePrefix(params.getTextDocument().getUri());
    // Parse and cache the AST immediately
    System.err.println("Did open");
    updateAST(uri);
  }

  @Override
  public void didClose(DidCloseTextDocumentParams params) {
    System.err.println("Did close");
    String uri = removePrefix(params.getTextDocument().getUri());
    removeAST(uri);
  }

  @Override
  public void didChange(DidChangeTextDocumentParams params) {
    String uri = removePrefix(params.getTextDocument().getUri());
    System.err.println("Did change");
    updateAST(uri);
  }

  @Override
  public void didSave(DidSaveTextDocumentParams params) {
    String uri = removePrefix(params.getTextDocument().getUri());
    System.err.println("Did save");
    expensiveChecks(uri);
  }

  @Override
  public CompletableFuture<Hover> hover(HoverParams params) {
    System.err.println("Did hover");
    return CompletableFuture.supplyAsync(() -> {
      String uri = removePrefix(params.getTextDocument().getUri());
      Position position = params.getPosition();

      // 1. Grab your pre-parsed AST from the cache
      ContractLibAst ast = getAST(uri);
      if (ast == null) {
        System.err.println("Empty AST: " + uri);
        return null; // No AST available yet
      }

      // 2. Query your AST using the line/character position provided by LazyVim
      //ASTNode node = ast.findNodeAt(position.getLine(), position.getCharacter());
      //if (node == null) {
      //  return null; // Cursor is on whitespace/empty area
      //}

      // 3. Build the hover card using Markdown strings
      MarkupContent content = new MarkupContent();
      content.setKind("markdown");
      content.setValue("Example Hover");

      return new Hover(content);
    });
  }

  @Override
  public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams params) {
    return CompletableFuture.supplyAsync(() -> {
      System.err.println("Provide completeion");

      List<CompletionItem> items = new ArrayList<>();

      // 1. Extract context data
      String uri = params.getTextDocument().getUri();
      Position position = params.getPosition();
      int line = position.getLine();
      int character = position.getCharacter();

      // 2. Mocking a context check: 
      // In a real application, you would fetch the document text using the URI 
      // and read the line up to the 'character' index to see what the user typed.

      // Example Completion Item 1: A standard Keyword
      /*
      CompletionItem keywordItem = new CompletionItem();
      keywordItem.setLabel("public");
      keywordItem.setKind(CompletionItemKind.Keyword);
      keywordItem.setDetail("Access modifier");
      keywordItem.setDocumentation("Declares a member visible to all classes.");
      items.add(keywordItem);
      */

      CompletionItem abstractions = new CompletionItem();
      abstractions.setLabel("Abstractions");
      abstractions.setKind(CompletionItemKind.Snippet);
      abstractions.setInsertText(
          """
              (declare-abstractions
                ((${1:package}.${2:abstractionName} ${3:0}))
                  (((${2:constructorName}$0))))
              """);
      abstractions.setInsertTextFormat(InsertTextFormat.Snippet);
      abstractions.setDetail("Declares new abstractions.");
      items.add(abstractions);

      CompletionItem selector = new CompletionItem();
      selector.setLabel("Selector");
      selector.setKind(CompletionItemKind.Snippet);
      selector.setInsertText(
          "(${1:identifer} ${2:Type})$0");
      selector.setInsertTextFormat(InsertTextFormat.Snippet);
      selector.setDetail("Declares new abstractions.");
      items.add(selector);

      CompletionItem contract = new CompletionItem();
      contract.setLabel("Contract");
      contract.setKind(CompletionItemKind.Snippet);
      contract.setInsertText(
          """
              (define-contract ${1:package}.${2:abstractionName}.${3:methodName} (
                ${4:arguments}
              ) ((
                (${5:pre})
                (${6:post})
              )))$0
                """);
      contract.setInsertTextFormat(InsertTextFormat.Snippet);
      contract.setDetail("Declares a new contract.");
      items.add(contract);

      CompletionItem contractArgument = new CompletionItem();
      contractArgument.setLabel("Contract Argument");
      contractArgument.setKind(CompletionItemKind.Snippet);
      contractArgument.setInsertText(
          "(${1:identifer} (${2|inout,in,out|} ${3:type}))$0");
      contractArgument.setInsertTextFormat(InsertTextFormat.Snippet);
      contractArgument.setDetail("Declares a new argument for a contract.");
      items.add(contractArgument);

      return Either.forLeft(items);
    });
  }

  // - 

  private void expensiveChecks(String uri) {

  }

  private void updateAST(String uri) {
    try {
      ContractLibGenerator generator = new ContractLibGenerator(messageContext.getMessageManager());
      ContractLibAst ast = generator.generateFromFile(uri);
      astCache.put(uri, ast);
      System.err.println("AST parsed (" + ast.abstractions().size() + "): " + uri);
    } catch (IOException e) {
      System.err.println(e);
    }
  }

  private ContractLibAst getAST(String uri) {
    return astCache.get(uri);
  }

  private void removeAST(String uri) {
    astCache.remove(uri);
  }

  private String removePrefix(String s) {
    return s.replace("file:", "");
  }
}
