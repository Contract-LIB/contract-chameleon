package org.contract_lib.lang.contract_lib.ast;

public record Argument(
    Sort sort,
    String identifier) implements ContractLibAstElement {
}
