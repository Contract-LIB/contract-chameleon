package org.contract_lib.lang.contract_lib.ast;

public record SortedVar(
    Symbol symbol,
    Sort sort) implements ContractLibAstElement {
}
