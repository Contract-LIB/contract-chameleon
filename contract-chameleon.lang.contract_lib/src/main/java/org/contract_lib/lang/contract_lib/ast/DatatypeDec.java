package org.contract_lib.lang.contract_lib.ast;

import java.util.List;

import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement.Inner;

public record DatatypeDec(
    List<Parameter> parameters,
    List<Constructor> constructors) implements Inner {
}
