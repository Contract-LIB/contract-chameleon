package org.contract_lib.adapters.lsp;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.io.IOException;

import org.eclipse.lsp4j.services.WorkspaceService;

import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.TextDocumentSyncKind;
import org.eclipse.lsp4j.TextDocumentContentChangeEvent;

import org.eclipse.lsp4j.DidChangeConfigurationParams;
import org.eclipse.lsp4j.DidChangeWatchedFilesParams;
import org.eclipse.lsp4j.services.WorkspaceService;

import org.contract_lib.contract_chameleon.contexts.MessageContext;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.generator.ContractLibGenerator;

public class ClibWorkspaceService implements WorkspaceService {

  // Maps a file URI (e.g., "file:///project/main.custom") to its parsed AST
  private final MessageContext messageContext;

  public ClibWorkspaceService(MessageContext messageContext) {
    this.messageContext = messageContext;
  }

  // Triggered when a user changes their IDE settings/configuration
  @Override
  public void didChangeConfiguration(DidChangeConfigurationParams params) {
    // Leave empty: Do nothing
  }

  // Triggered when a watched file changes on disk (e.g., via a git pull or file explorer)
  @Override
  public void didChangeWatchedFiles(DidChangeWatchedFilesParams params) {
    // Leave empty: Do nothing
  }
}
