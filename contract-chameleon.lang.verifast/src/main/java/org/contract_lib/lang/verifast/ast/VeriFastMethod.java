package org.contract_lib.lang.verifast.ast;

import java.util.List;
import java.util.Optional;

public record VeriFastMethod(
    VeriFastContract contract,
    String name,
    Optional<VeriFastType> resultType,
    List<VeriFastArgument> arguments,
    boolean isStatic,
    Optional<List<VeriFastJavaExpression>> body //if no body method is abstract
) {

}
