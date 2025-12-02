package org.contract_lib.contract_chameleon.cl;

import java.util.List;

import org.contract_lib.contract_chameleon.error.ChameleonMessageManager;

/// A command line argument defines special arguments for adapters.
///
/// Every ideally adapters should use the provided default arguments,
/// but are also able to define their own by providing a service for this interface.
public interface CommandLineArgument {

  /// All possible labels, by which the command line arguments can be identified.
  List<String> getLabels();

  /// Help desciption of the CommandLineArgument.
  ///
  /// This description is automatically added at the end of the help,
  /// when an adapters declares that it supports an argument.
  String getDescription();

  /** The {@ArgumentParser} passes the following arguments,
   *  until a new label is detected.
   *
   * Default implementation checks that the provided list is empty.
   **/
  default void parseArguments(
      ChameleonMessageManager messageManager,

      ) {


  }

  /// The parsed values can be validated, default does not check anything.
  default void argumentValidation(
}}
