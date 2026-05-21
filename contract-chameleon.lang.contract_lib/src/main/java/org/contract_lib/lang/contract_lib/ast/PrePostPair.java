package org.contract_lib.lang.contract_lib.ast;

public record PrePostPair(
    Term pre,
    Term post) implements ContractLibAstElement {
}
