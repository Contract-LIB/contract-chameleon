package org.contract_lib.lang.contract_lib.ast;

import java.util.List;

public record Constructor(
    Symbol identifier,
    List<SelectorDec> selectors) implements ContractLibAstElement {
}
