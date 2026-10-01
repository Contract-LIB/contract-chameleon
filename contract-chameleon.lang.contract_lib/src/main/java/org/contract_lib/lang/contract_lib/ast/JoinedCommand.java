package org.contract_lib.lang.contract_lib.ast;

import java.util.List;
import java.util.function.Function;

/**
 * This command is not diretly part of the AST,
 * but is needed to join multiple AST nodes together,
 * when they are created in the same command call. 
 * 
 * At the moment this counts for {@code Datatype}, {@code Abstraction} {@code FunctionDec}.
 * NOTE:There always has to be at least one command. */
public record JoinedCommand<C extends JoinableCommand<C>>(List<C> commands) implements Command {

  @Override
  public <R> R perform(
      Function<Abstraction, R> decAbstraction,
      Function<JoinedCommand<Abstraction>, R> decAbstractions,
      Function<Constant, R> decConstant,
      Function<Datatype, R> decDatatype,
      Function<JoinedCommand<Datatype>, R> decDatatypes,
      Function<FunctionDec, R> decFunction,
      Function<Parameter, R> decParameter,
      Function<SortDec, R> decSort,
      Function<SortDec, R> defSort,
      Function<Contract, R> defContract,
      Function<FunctionDec, R> defFunction,
      Function<FunctionDec, R> defFunctionRec,
      Function<JoinedCommand<FunctionDec>, R> defFunctionsRec,
      Function<Assert, R> assertion) {
    return commands.getFirst().select(decAbstractions, decDatatypes, defFunctionsRec).apply(this);
  }

}
