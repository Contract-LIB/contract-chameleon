package org.contract_lib.contract_chameleon.cl_arguments;

import java.util.List;
import java.util.Optional;

import org.contract_lib.contract_chameleon.cl.CommandLineArgument;
import org.contract_lib.contract_chameleon.error.ChameleonMessageManager;

public final class OutputPath implements CommandLineArgument {

  /// All possible labels, by which the command line arguments can be identified
  public List<String> getLabels() {
    return List.of("-o", "--output");
  }

  /// Help desciption of the CommandLineArgument
  public String getDescription() {
    return "Overwrite the directory path of the output.";
  }

  /// The parsed values can be validated 
  public void argumentValidation(
      ChameleonMessageManager messageManager,
      List<String> parsedValues) {
  }

}
