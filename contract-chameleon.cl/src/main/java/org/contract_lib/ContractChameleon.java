package org.contract_lib;

import org.contract_lib.contract_chameleon.cl.ArgumentParser;
import org.contract_lib.contract_chameleon.error.ChameleonMessageManager;
import org.contract_lib.contract_chameleon.error.ChameleonMessageType;
import org.contract_lib.contract_chameleon.error.SimpleErrorMessage;

class ContractChameleon {

  public static void main(String[] args) {

    ChameleonMessageManager messageManager = new ChameleonMessageManager();

    messageManager.report(new SimpleErrorMessage(
        String.format("Available Classpath: %s", System.getProperty("java.class.path")),
        ChameleonMessageType.INFO, null));
    messageManager.report(new SimpleErrorMessage(
        String.format("Current Working Directory: %s", System.getProperty("user.dir")),
        ChameleonMessageType.INFO, null));

    ArgumentParser argumentParser = new ArgumentParser();

    argumentParser.parseArguments(args, messageManager);

    argumentParser
        .getAdapter()
        .ifPresent(a -> a.perform(messageManager, argumentParser, args));

    messageManager.writeStdErr();
  }
}
