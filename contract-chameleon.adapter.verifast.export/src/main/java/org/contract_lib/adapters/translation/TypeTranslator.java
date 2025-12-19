package org.contract_lib.adapters.translation;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import org.contract_lib.lang.contract_lib.ast.Sort;

import org.contract_lib.lang.verifast.ast.VeriFastType;

public final class TypeTranslator {

  private final Map<String, TypeTranslation> translations;

  public TypeTranslator(List<TypeTranslation> translations) {
    this.translations = new HashMap<>();
    for (TypeTranslation t : translations) {
      this.translations.put(t.getClibSort().getName(), t);
    }
  }

  public void addTranslation(TypeTranslation translation) {
    String identifier = translation.getClibSort().getName();
    if (this.translations.containsKey(identifier)) {
      //TODO: print error
      System.err.println("ERROR: the following type is ambiguous: " + identifier);
    }
    this.translations.put(identifier, translation);
  }

  public VeriFastType translate(Sort sort) {
    TypeTranslation t = this.translations.get(sort.getName());
    if (t == null) {
      System.err.println(String.format("ERROR: Translation not found for sort: %s", sort.getName()));
      //TODO: print proper error
      return new VeriFastType.VeriFastClass("ErrorType", List.of());
    } else {
      return t.getVerifastType(this, sort);
    }
  }

  public String getDefaultMethodBody(Sort sort) {
    TypeTranslation t = this.translations.get(sort.getName());
    if (t == null) {
      return "null";
    } else {
      return t.getDefaultValue();
    }
  }
}
