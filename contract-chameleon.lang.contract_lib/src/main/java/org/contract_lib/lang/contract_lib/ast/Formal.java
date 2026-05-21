package org.contract_lib.lang.contract_lib.ast;

public record Formal(
    Symbol identifier,
    ArgumentMode argumentMode,
    Sort sort) implements ContractLibAstElement {
}
