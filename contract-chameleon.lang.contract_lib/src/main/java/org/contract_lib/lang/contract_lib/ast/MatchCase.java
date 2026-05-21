
package org.contract_lib.lang.contract_lib.ast;

public record MatchCase(
    Pattern pattern,
    Term term) implements ContractLibAstElement {
}
