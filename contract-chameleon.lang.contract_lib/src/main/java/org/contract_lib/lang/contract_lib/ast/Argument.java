package org.contract_lib.lang.contract_lib.ast;

import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement.Inner;

//TODO: Use sorted var
@Deprecated
public record Argument(
    Sort sort,
    String identifier) implements Inner {
}
