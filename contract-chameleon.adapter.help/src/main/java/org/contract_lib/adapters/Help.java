package org.contract_lib.adapters;

import org.contract_lib.contract_chameleon.Adapter;
import org.contract_lib.contract_chameleon.AdapterMap;

import com.google.auto.service.AutoService;

@AutoService(Adapter.class)
public final class Help extends Adapter implements HelpMessage {

  private static String ADAPTER_NAME = "help";

  private static String HELP_MESSAGE = """
      Contract Chameleon Help

      contract-chameleon [adapter] [arguments]

      Print help for adapter:

      contract-chameleon help [adapter]
      """;

  private static String ADAPTER_DESCRIPTION = """
      Adapter for providing help
      """;

  @Override
  public String getAdapterName() {
    return ADAPTER_NAME;
  }

  @Override
  public void perform(String[] args) {

    AdapterMap<HelpMessage> helpMap = new AdapterMap<>(HelpMessage.class);
    AdapterMap<Adapter> adapterMap = new AdapterMap<>(Adapter.class);

    if (args.length <= 1) {
      System.out.println(this.help());
      System.out.println(adapterMap.adapterList());
      return;
    }
    if (args.length > 2) {
      System.out.println(this.help());
      System.out.println(adapterMap.adapterList());
      System.err.println("More arguments than one adapter name are ignored.");
      return;
    }

    String adapterName = args[1];

    if (adapterName.equals(ADAPTER_NAME)) {
      System.out.println(this.help());
      System.out.println(adapterMap.adapterList());
      return;
    }

    HelpMessage helper = helpMap.get(adapterName);
    if (helper != null) {
      System.out.println(helper.help());
      return;
    }

    Adapter adapter = adapterMap.get(adapterName);
    if (adapter != null) {
      System.err.println(String.format("No help provided for the adapter: %s", adapterName));
      return;
    }
  }

  @Override
  public String help() {
    return HELP_MESSAGE;
  }

  @Override
  public String adapterDescription() {
    return ADAPTER_DESCRIPTION;
  }
}
