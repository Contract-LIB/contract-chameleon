package org.contract_lib.lang.contract_lib.ast;

import java.util.List;

import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement.Inner;

public record Constructor(
    Symbol identifier,
    List<SelectorDec> selectors) implements Inner {
}
