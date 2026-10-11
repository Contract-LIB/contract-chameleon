package org.contract_lib.lang.contract_lib.ast;

import java.util.function.Function;

/**
 * Interface to access the specific functions of the generic type {@code C},
 * when working with an arbitrary {@code JoinedCommand<C>}.
 */
public interface JoinableCommand<C extends JoinableCommand<C>> extends Command {

  /// Select a command handler from the provided matching the type.
  public <R> Function<JoinedCommand<C>, R> select(
      Function<JoinedCommand<Abstraction>, R> decAbstractions,
      Function<JoinedCommand<Datatype>, R> decDatatape,
      Function<JoinedCommand<FunctionDec>, R> decFunctionsRec);
}
