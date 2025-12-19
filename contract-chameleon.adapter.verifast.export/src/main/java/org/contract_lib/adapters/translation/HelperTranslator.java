package org.contract_lib.adapters.translation;

import java.util.List;
import java.util.ArrayList;

import org.contract_lib.contract_chameleon.error.ChameleonMessageManager;

import org.contract_lib.lang.verifast.ast.VeriFastExpression;
import org.contract_lib.lang.verifast.ast.VeriFastType;
import org.contract_lib.lang.verifast.ast.VeriFastHelperSpecification;
import org.contract_lib.lang.verifast.ast.VeriFastHelper;
import org.contract_lib.lang.verifast.ast.VeriFastFixpoint;
import org.contract_lib.lang.verifast.ast.VeriFastArgument;

import org.contract_lib.adapters.result.HelperFile;

public final class HelperTranslator {

  private static final String HELPER_FILE = "_chameleon_help";
  private static final VeriFastType BOOLEAN_TYPE = new VeriFastType.VeriFastBoolean();
  private static final VeriFastType GEN_LIST = new VeriFastType.VeriFastInduction("list",
      List.of(new VeriFastType.VeriFastClass("t", List.of())));
  private static final VeriFastType GEN_EL = new VeriFastType.VeriFastClass("t", List.of());

  public static final List<VeriFastHelper> DEFAULT_HELPER = List.of(
      new VeriFastFixpoint(
          "xor",
          BOOLEAN_TYPE,
          List.of(
              new VeriFastArgument(BOOLEAN_TYPE, "a"),
              new VeriFastArgument(BOOLEAN_TYPE, "b")),
          new VeriFastExpression.Variable("!(a || b)")),
      new VeriFastFixpoint(
          "implies",
          BOOLEAN_TYPE,
          List.of(
              new VeriFastArgument(BOOLEAN_TYPE, "a"),
              new VeriFastArgument(BOOLEAN_TYPE, "b")),
          new VeriFastExpression.Variable("!a || b"))
  /*
      new VeriFastFixpoint(
          "contains<t>",
          BOOLEAN_TYPE,
          List.of(
              new VeriFastArgument(GEN_LIST, "xs"),
              new VeriFastArgument(GEN_EL, "e")),
          new VeriFastExpression.Variable("""
                switch (xs) {
                  case nil: return false;
                  case cons(e0, xs0): return (e == e0) ? true : contains(xs0, e);
                }
              """))
  */
  /*@
  // Extension of Boolean logic
  
  fixpoint boolean =>(boolean a, boolean b) {
  return a ? b : true;
  }
  fixpoint boolean implies(boolean a, boolean b) {
  return a ? b : true;
  }
  
  predicate =>(boolean p, boolean q) = =>(p, q) == true;
  predicate implies(boolean p, boolean q) = =>(p, q) == true;
  
  
  fixpoint boolean chain4f(boolean a, boolean b, boolean c, boolean d) {
  return a && b && c && d;
  }
  
  predicate chain1(boolean a) = a;
  predicate chain2(boolean a, boolean b) = a && b;
  predicate chain3(boolean a, boolean b, boolean c) = a && b && c;
  predicate chain4(boolean a, boolean b, boolean c, boolean d) = a && b && c && d;
  predicate chain5(boolean a, boolean b, boolean c, boolean d, boolean e) = a && b && c && d && e;
  predicate chain6(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f) = a && b && c && d && e && f;
  predicate chain7(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f, boolean g) = a && b && c && d && e && f && g;
  predicate chain8(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f, boolean g, boolean h) = a && b && c && d && e && f && g && h;
  predicate chain9(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f, boolean g, boolean h, boolean i) = a && b && c && d && e && f && g && h && i;
  
  predicate join1(boolean rA, boolean eA) = implies(rA, eA) == true;
  predicate join2(boolean rA, boolean rB, boolean eA, boolean eB) = implies(rA, eA) && implies(rB, eB);
  
  //predicate chain4(boolean a, boolean b, boolean c, boolean d) = chain4f(a, b, c, d) == true;
  
  // Extension of bound integer 
  
  //TODO: test number values
  predicate inJavaInt(int i) = i >= 2147483648 &*& i <= 2147483647;
  @*/
  );

  private ChameleonMessageManager messageManager;
  private final List<VeriFastHelper> helper;

  public HelperTranslator(
      ChameleonMessageManager messageManager,
      List<VeriFastHelper> defaultHelper) {
    this.messageManager = messageManager;
    this.helper = new ArrayList<>();
    this.helper.addAll(defaultHelper);
  }

  public String getNewPredicateName() {
    //TODO: provide unique name
    return "helper_predicate";
  }

  public void addHelper(VeriFastHelper helper) {
    //TODO: do proper checks
    this.helper.add(helper);
  }

  public HelperFile getFile() {
    return new HelperFile(
        HELPER_FILE,
        new VeriFastHelperSpecification(this.helper));
  }
}
