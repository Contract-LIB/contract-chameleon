
package org.contract_lib.lang.contract_lib.ast;

//TODO: Replace all occurences of Symbol with string, to simplify AST
public record Symbol(
    String identifier) implements ContractLibAstElement {
}
