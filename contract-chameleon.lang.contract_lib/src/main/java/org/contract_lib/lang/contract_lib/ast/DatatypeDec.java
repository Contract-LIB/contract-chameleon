package org.contract_lib.lang.contract_lib.ast;

import java.util.List;

public record DatatypeDec(
    List<Parameter> parameters,
    List<Constructor> constructors) implements ContractLibAstElement {
}
