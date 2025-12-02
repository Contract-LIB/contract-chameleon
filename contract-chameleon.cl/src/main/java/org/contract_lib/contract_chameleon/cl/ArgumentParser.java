package org.contract_lib.contract_chameleon.cl;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class ArgumentParser {

  public void parseArguments(String[] args) {

    ServiceLoader<CommandLineArgument> commandLineArguments = ServiceLoader.load(CommandLineArgument.class);
    Map<String, CommandLineArgument> parseMap = new HashMap<>();

    //commandLineArguments.stream().map();
    //parseMap.get

    for (String s : args) {
      System.err.println(s);
    }
  }

}
