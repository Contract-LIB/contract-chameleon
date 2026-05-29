package org.contract_lib.adapters;

import org.contract_lib.contract_chameleon.Adapter;
import org.contract_lib.contract_chameleon.adapters.CheckerAdapter;

import com.google.auto.service.AutoService;

@AutoService(Adapter.class)
public final class ContractLibCheckerAdapter extends CheckerAdapter {

  public String getAdapterName() {
    return "contract-lib-identifier-checker";
  }

  @Override
  public CheckerAdapterResult performCheck() {

    this.getMessageContext().logError("No checks performed!");

    return CheckerAdapterResult.FAILURE;
  }
}
