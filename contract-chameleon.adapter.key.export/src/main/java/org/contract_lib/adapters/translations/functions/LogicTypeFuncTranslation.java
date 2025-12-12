
package org.contract_lib.adapters.translations.functions;

import java.util.List;

import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import org.contract_lib.adapters.translations.FuncTranslation;
import org.contract_lib.lang.contract_lib.ast.Sort;
import org.contract_lib.lang.contract_lib.ast.Symbol;
import org.contract_lib.lang.contract_lib.ast.Term;
import org.contract_lib.lang.key.ast.KeySort;

public record LogicTypeFuncTranslation(
    KeySort.Custom sort,
    String constructorName) implements FuncTranslation {

  public Term.Identifier.IdentifierValue getClibIdentifier() {
    return new Term.Identifier.IdentifierValue(new Symbol(this.constructorName));
  }

  public Expression getJmlTerm(List<Expression> parameters) {
    //TODO: Throw proper error
    System.err.println(
        String.format("WARNING: no args count check for constructor of '%s': given %s",
            sort,
            parameters));
    return new MethodCallExpr(null,
        "\\dl_" + constructorName,
        NodeList.nodeList(parameters));
  }

  public List<Sort> getClibParameterTypes() {
    return List.of();
  }

  public List<Type> getJmlParameterTypes() {
    //TODO: to impl.
    return List.of();
  }

  public Sort getClibResultType() {
    return new Sort.Type(sort.name());
  }

  public Type getJmlResultType() {
    return new ClassOrInterfaceType(null, sort.name());
  }
}
