package org.contract_lib.lang.contract_lib.ast;

import java.util.List;

public record Contract(
    Symbol identifier,
    List<Formal> formals,
    List<PrePostPair> pairs) implements ContractLibAstElement {
}
