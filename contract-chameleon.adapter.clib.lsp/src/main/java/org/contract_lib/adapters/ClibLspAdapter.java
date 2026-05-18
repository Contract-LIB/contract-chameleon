package org.contract_lib.adapters;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.nio.file.Path;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageServer;

import org.contract_lib.adapters.lsp.ClibLanguageServer;

import org.contract_lib.contract_chameleon.Adapter;
import org.contract_lib.contract_chameleon.SharedContextManager.InterfaceProvidedContext;
import org.contract_lib.contract_chameleon.adapters.ExportAdapter;
import org.contract_lib.contract_chameleon.contexts.MessageContext;
import org.contract_lib.contract_chameleon.contexts.ResultDirectoryContext.Dir;
import org.contract_lib.contract_chameleon.contexts.ResultDirectoryContext.TranslationResult;

import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.generator.ContractLibGenerator;

import com.google.auto.service.AutoService;

@AutoService(Adapter.class)
public final class ClibLspAdapter extends Adapter {

  public String getAdapterName() {
    return "clib-lsp";
  }

  @Override
  public Set<Class<? extends InterfaceProvidedContext>> argumentContextsFromInterface() {
    return Set.of();
  }

  @Override
  protected void perform() {
    System.err.println("loading lsp");
    LanguageServer lsp = new ClibLanguageServer(getMessageContext());

    Launcher<LanguageClient> launcher = LSPLauncher.createServerLauncher(
        lsp,
        System.in,
        System.out);
    Future<Void> future = launcher.startListening();

    System.err.println("lsp started");

    try {
      future.get();
    } catch (ExecutionException | InterruptedException e) {
      // It is totally fine for this future to be interrupted to exit the lsp
    }
  }
}
