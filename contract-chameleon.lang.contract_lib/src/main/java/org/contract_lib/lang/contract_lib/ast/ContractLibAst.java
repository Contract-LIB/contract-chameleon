package org.contract_lib.lang.contract_lib.ast;

import java.util.List;

public record ContractLibAst(
    List<Datatype> datatypes,
    List<Abstraction> abstractions,
    List<SortDec> sorts,
    List<Parameter> sortParameter,

    List<FunctionDec> functions,
    List<Constant> constants,

    List<Contract> contracts,

    List<Assert> asserts) implements ContractLibAstElement {
}
