package org.contract_lib.adapters.translation.types;

import java.util.List;

import org.contract_lib.adapters.translation.TypeTranslation;
import org.contract_lib.adapters.translation.TypeTranslator;
import org.contract_lib.lang.contract_lib.ast.Sort;

import org.contract_lib.lang.verifast.ast.VeriFastExpression;
import org.contract_lib.lang.verifast.ast.VeriFastType;

public final class IntTranslation implements TypeTranslation {

  public Sort getClibSort() {
    return new Sort.Type("Int");
  }

  public VeriFastType getVerifastType(
      TypeTranslator translator,
      Sort sort) {
    return new VeriFastType.VeriFastInteger();
  }

  public List<VeriFastExpression> getHelper() {
    return List.of();
  }

  public String getDefaultValue() {
    return "0";
  }
}
