package org.contract_lib.adapters.lsp;

import org.contract_lib.contract_chameleon.contexts.MessageContext;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ClibLanguageServer implements LanguageServer {

  private final TextDocumentService textDocumentService;

  public ClibLanguageServer(MessageContext messageContext) {
    this.textDocumentService = new ClibTextDocumentService(messageContext);
    this.workspaceService = new ClibWorkspaceService(messageContext);
  }

  private final WorkspaceService workspaceService;

  @Override
  public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
    // Define what your server can do (capabilities)
    ServerCapabilities capabilities = new ServerCapabilities();
    capabilities.setHoverProvider(true);
    capabilities.setCompletionProvider(new CompletionOptions(true, List.of("(")));
    capabilities.setTextDocumentSync(TextDocumentSyncKind.Full);

    return CompletableFuture.completedFuture(new InitializeResult(capabilities));
  }

  @Override
  public CompletableFuture<Object> shutdown() {
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public void exit() {
    System.exit(0);
  }

  @Override
  public TextDocumentService getTextDocumentService() {
    return textDocumentService;
  }

  @Override
  public WorkspaceService getWorkspaceService() {
    return workspaceService;
  }
}
