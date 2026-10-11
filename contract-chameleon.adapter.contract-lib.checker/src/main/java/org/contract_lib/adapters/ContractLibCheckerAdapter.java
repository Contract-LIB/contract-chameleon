package org.contract_lib.adapters;

import org.contract_lib.contract_chameleon.Adapter;
import org.contract_lib.contract_chameleon.adapters.CheckerAdapter;
import org.contract_lib.lang.contract_lib.context_provider.AppliedAstExtensionsContextProvider;
import org.contract_lib.lang.contract_lib.context_provider.AvailableSortIdentifierContextProvider;
import org.contract_lib.lang.contract_lib.context_provider.ContractLibAstContextProvider;
import org.contract_lib.lang.contract_lib.context_provider.FileIdentifierContextProvider;
import org.contract_lib.lang.contract_lib.context_provider.ast_extensions.AccessSortIdentifierContextProvider;
import org.contract_lib.lang.contract_lib.context_provider.ast_extensions.DefinedSortIdentifierContextProvider;
import org.contract_lib.lang.contract_lib.context_provider.ast_extensions.CommandOrderContextProvider;
import org.contract_lib.lang.contract_lib.context_provider.ast_extensions.FilePositionLinkerContextProvider;
import org.contract_lib.lang.contract_lib.contexts.AppliedAstExtensionsContext;
import org.contract_lib.lang.contract_lib.contexts.AvailableSortIdentifierContext;
import org.contract_lib.lang.contract_lib.contexts.CurrentFileIdentifierContext;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.AccessSortIdentifierContext;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.DefinedSortIdentifierContext;
import org.contract_lib.lang.contract_lib.contexts.ast_extensions.FilePositionLinkerContext;
import org.contract_lib.lang.contract_lib.tester.TestSorts;

import com.google.auto.service.AutoService;

@AutoService(Adapter.class)
public final class ContractLibCheckerAdapter extends CheckerAdapter {

  public String getAdapterName() {
    return "contract-lib-identifier-checker";
  }

  @Override
  public CheckerAdapterResult performCheck() {

    // TODO: Remove unchecked unwrap
    AppliedAstExtensionsContext appliedExtensions = getContext(new AppliedAstExtensionsContextProvider()).get();

    // Adding required extensions
    appliedExtensions.addAstExtension(new FilePositionLinkerContextProvider());
    appliedExtensions.addAstExtension(new CommandOrderContextProvider());

    // TODO: Remove unchecked unwrap
    FilePositionLinkerContext parentLinkerContext = getContext(new FilePositionLinkerContextProvider()).get();

    // TODO: Remove unchecked unwrap
    CurrentFileIdentifierContext fileIdentifierContext = this
        .getContext(new FileIdentifierContextProvider())
        .get();
    DefinedSortIdentifierContext definedSortIdentifierContext = this
        .getContext(new DefinedSortIdentifierContextProvider())
        .get();
    AccessSortIdentifierContext accessSortIdentifierContext = this
        .getContext(new AccessSortIdentifierContextProvider())
        .get();
    AvailableSortIdentifierContext availableSortIdentifierContext = this
        .getContext(new AvailableSortIdentifierContextProvider())
        .get();

    // TODO: Remove unchecked unwrap
    this
        .getContext(new ContractLibAstContextProvider())
        .get();

    TestSorts testSorts = new TestSorts(this.getMessageContext());
    return testSorts.testSorts(
        fileIdentifierContext,
        parentLinkerContext,
        definedSortIdentifierContext,
        accessSortIdentifierContext,
        availableSortIdentifierContext);
  }
}
