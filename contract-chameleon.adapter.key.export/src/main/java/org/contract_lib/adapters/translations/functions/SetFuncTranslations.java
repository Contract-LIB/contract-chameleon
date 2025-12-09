package org.contract_lib.adapters.translations.functions;

import java.util.List;

import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;

import org.contract_lib.adapters.translations.FuncProvider;
import org.contract_lib.adapters.translations.FuncTranslation;
import org.contract_lib.lang.contract_lib.ast.Sort;
import org.contract_lib.lang.contract_lib.ast.Term;

public record SetFuncTranslations() implements FuncProvider {

  static final Sort CLIB_SET_TYPE = new Sort.Type("Set");
  static final Sort CLIB_GENERIC_TYPE = new Sort.Type("A");
  static final Sort CLIB_BOOL_TYPE = new Sort.Type("Bool");
  static final Sort CLIB_INT_TYPE = new Sort.Type("Int");

  static final Type JML_SET_TYPE = new ClassOrInterfaceType(null, new SimpleName("\\map"), null);
  static final Type JML_GENERIC_TYPE = new ClassOrInterfaceType(null, new SimpleName("A"), null);
  static final Type JML_BOOL_TYPE = PrimitiveType.booleanType();
  static final Type JML_INT_TYPE = PrimitiveType.intType();

  public List<FuncTranslation> getAll() {
    return List.of(
        new FuncTranslation.MethodCall(
            "set.empty",
            "\\dl_sEmpty",
            List.of(),
            List.of(),
            JML_SET_TYPE,
            CLIB_SET_TYPE),

        new FuncTranslation.MethodCall(
            "set.singleton",
            "\\dl_sSingleton",
            List.of(CLIB_GENERIC_TYPE),
            List.of(JML_GENERIC_TYPE),
            JML_SET_TYPE,
            CLIB_SET_TYPE),

        new FuncTranslation.MethodCall(
            "set.union",
            "\\dl_sUnion",
            List.of(CLIB_SET_TYPE, CLIB_SET_TYPE),
            List.of(JML_SET_TYPE, JML_SET_TYPE),
            JML_SET_TYPE,
            CLIB_SET_TYPE),

        new FuncTranslation.MethodCall(
            "set.inter",
            "\\dl_sIntersect",
            List.of(CLIB_SET_TYPE, CLIB_SET_TYPE),
            List.of(JML_SET_TYPE, JML_SET_TYPE),
            JML_SET_TYPE,
            CLIB_SET_TYPE),

        new FuncTranslation.MethodCall(
            "set.minus",
            "\\dl_sSetMinus",
            List.of(CLIB_SET_TYPE, CLIB_SET_TYPE),
            List.of(JML_SET_TYPE, JML_SET_TYPE),
            JML_SET_TYPE,
            CLIB_SET_TYPE),

        new FuncTranslation.MethodCall(
            "set.member",
            "\\dl_sElementOf",
            List.of(CLIB_SET_TYPE, CLIB_GENERIC_TYPE),
            List.of(JML_SET_TYPE, JML_GENERIC_TYPE),
            JML_BOOL_TYPE,
            CLIB_BOOL_TYPE),
        new FuncTranslation.MethodCall(
            "set.subset",
            "\\dl_sSubset",
            List.of(CLIB_SET_TYPE, CLIB_SET_TYPE),
            List.of(JML_SET_TYPE, JML_SET_TYPE),
            JML_BOOL_TYPE,
            CLIB_BOOL_TYPE),
        new FuncTranslation.MethodCall(
            "set.disjunct",
            "\\dl_sDisjoint",
            List.of(CLIB_SET_TYPE, CLIB_SET_TYPE),
            List.of(JML_SET_TYPE, JML_SET_TYPE),
            JML_BOOL_TYPE,
            CLIB_BOOL_TYPE),

        new FuncTranslation.MethodCall(
            "set.card",
            "\\dl_sCard",
            List.of(CLIB_SET_TYPE),
            List.of(JML_SET_TYPE),
            JML_INT_TYPE,
            CLIB_INT_TYPE));
  }
}
