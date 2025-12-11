package org.contract_lib.contract_chameleon;

import org.contract_lib.contract_chameleon.error.ChameleonMessageManager;

public abstract class Adapter implements AdapterId {

  //TODO: Remove args
  public abstract void perform(
      ChameleonMessageManager messageManager,
      AdapterArgumentProvider adapterProvider,
      String[] args);
}
