
package org.contract_lib.lang.contract_lib.label;

import java.util.Set;

public final record Identifier<M extends IdentifierMode, I extends IdentifierScope, T extends IdentifierType>(
    Set<String> identifier) {
}
