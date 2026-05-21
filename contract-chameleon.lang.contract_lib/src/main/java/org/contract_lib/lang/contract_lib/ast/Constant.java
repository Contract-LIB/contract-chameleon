package org.contract_lib.lang.contract_lib.ast;

public record Constant(
    Symbol symbol,
    Sort sort) implements ContractLibAstElement {
}
