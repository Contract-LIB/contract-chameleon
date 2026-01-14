package org.contract_lib.lang.verifast.ast;

import java.util.List;

public record VeriFastConstructor(
    VeriFastContract contract,
    List<VeriFastArgument> arguments,
    List<VeriFastJavaExpression> body) {
}
