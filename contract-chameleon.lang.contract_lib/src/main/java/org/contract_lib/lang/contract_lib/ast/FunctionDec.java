package org.contract_lib.lang.contract_lib.ast;

import java.util.List;

public record FunctionDec(
    Symbol name,
    List<SortDec.Parameter> params,
    List<Sort> arguments,
    Sort result) implements ContractLibAstElement {
}
