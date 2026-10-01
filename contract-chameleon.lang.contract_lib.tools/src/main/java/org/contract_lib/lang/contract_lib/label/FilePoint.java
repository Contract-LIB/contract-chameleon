package org.contract_lib.lang.contract_lib.label;

/// A char position in the source file.
///
/// @param line starting at line 1.
/// @param charIndex starting at index 0.
public final record FilePoint(
    int line,
    int charIndex) {
}
