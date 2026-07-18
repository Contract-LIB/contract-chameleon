package org.contract_lib.lang.contract_lib.ast;

import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement.Inner;

//TODO: Merge with SelectoreDec?
public record SelectorDec(
    Symbol symbol,
    Sort sort) implements Inner {
}
