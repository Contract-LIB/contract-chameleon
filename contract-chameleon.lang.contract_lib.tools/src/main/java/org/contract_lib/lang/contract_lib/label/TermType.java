
package org.contract_lib.lang.contract_lib.label;

import org.contract_lib.lang.contract_lib.ast.Sort;
import org.contract_lib.lang.contract_lib.ast.Term;

public final class TermType {

  private record Pos(
      int line,
      int index) {
  }

  private LabelWrapper<Pos> lables = new LabelWrapper<>();

  public Sort getSort(Term t) {
    //TODO: To implement
    return new Sort.Type("TODO:");
  }
}
