package org.contract_lib.lang.contract_lib.ast;

public record VarBinding(
    Symbol name,
    Term type) implements ContractLibAstElement {
}
