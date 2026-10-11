
package org.contract_lib.lang.contract_lib.ast;

import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement.Inner;

//TODO: Replace all occurences of Symbol with string, to simplify AST
public record Symbol(
    String identifier) implements Inner {
}
