package org.contract_lib.lang.contract_lib.ast;

import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement.Inner;

public record Formal(
    Symbol identifier,
    ArgumentMode argumentMode,
    Sort sort) implements Inner {
}
