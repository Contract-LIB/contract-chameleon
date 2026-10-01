package org.contract_lib.lang.contract_lib.label;

import java.util.List;

import org.contract_lib.lang.contract_lib.ast.Sort;

/// 
///
/// Contract-LIB terms are well sorted,
/// so to each term, a signature can be associated.
public record TermTypeSignature(List<Sort> in, List<Sort> ret) {
}
