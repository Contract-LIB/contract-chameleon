
package org.contract_lib.lang.contract_lib.ast;

import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement.Inner;

public record MatchCase(
    Pattern pattern,
    Term term) implements Inner {
}
