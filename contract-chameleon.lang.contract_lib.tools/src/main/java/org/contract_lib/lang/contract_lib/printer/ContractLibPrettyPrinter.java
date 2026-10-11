
package org.contract_lib.lang.contract_lib.printer;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.contract_lib.lang.contract_lib.ast.Abstraction;
import org.contract_lib.lang.contract_lib.ast.ArgumentMode;
import org.contract_lib.lang.contract_lib.ast.Constructor;
import org.contract_lib.lang.contract_lib.ast.Contract;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.ast.Formal;
import org.contract_lib.lang.contract_lib.ast.MatchCase;
import org.contract_lib.lang.contract_lib.ast.Numeral;
import org.contract_lib.lang.contract_lib.ast.Parameter;
import org.contract_lib.lang.contract_lib.ast.Pattern;
import org.contract_lib.lang.contract_lib.ast.PrePostPair;
import org.contract_lib.lang.contract_lib.ast.Quantor;
import org.contract_lib.lang.contract_lib.ast.SelectorDec;
import org.contract_lib.lang.contract_lib.ast.Sort;
import org.contract_lib.lang.contract_lib.ast.SortedVar;
import org.contract_lib.lang.contract_lib.ast.Symbol;
import org.contract_lib.lang.contract_lib.ast.Term;
import org.contract_lib.lang.contract_lib.ast.VarBinding;
import org.contract_lib.lang.contract_lib.ast.Pattern.Case;
import org.contract_lib.lang.contract_lib.ast.Pattern.WithParameters;
import org.contract_lib.lang.contract_lib.ast.Sort.ParametricType;
import org.contract_lib.lang.contract_lib.ast.Sort.Type;
import org.contract_lib.lang.contract_lib.ast.SortDec;
import org.contract_lib.lang.contract_lib.ast.Term.Attributes;
import org.contract_lib.lang.contract_lib.ast.Term.BooleanLiteral;
import org.contract_lib.lang.contract_lib.ast.Term.LetBinding;
import org.contract_lib.lang.contract_lib.ast.Term.MatchBinding;
import org.contract_lib.lang.contract_lib.ast.Term.MethodApplication;
import org.contract_lib.lang.contract_lib.ast.Term.NumberLiteral;
import org.contract_lib.lang.contract_lib.ast.Term.Old;
import org.contract_lib.lang.contract_lib.ast.Term.QuantorBinding;
import org.contract_lib.lang.contract_lib.ast.Term.SpecConstant;
import org.contract_lib.lang.contract_lib.ast.Term.Identifier.IdentifierAs;
import org.contract_lib.lang.contract_lib.ast.Term.Identifier.IdentifierValue;

public final class ContractLibPrettyPrinter {

  private Writer writer;
  private int indentation;

  private static final int DEFAULT_INDENTATION = 4;

  private static final String SPACE = " ";
  private static final String BRACKET_OPEN = "(";
  private static final String BRACKET_CLOSE = ")";

  private static final String DECLARE_ABSTRACTIONS = "declare-abstractions";
  private static final String DEFINE_CONTRACT = "define-contract";

  private static final String ARGUMENT_IN = "in";
  private static final String ARGUMENT_INOUT = "inout";
  private static final String ARGUMENT_OUT = "out";

  private static final String QUANTOR_ALL = "forall";
  private static final String QUANTOR_EXISTS = "exists";
  private static final String LET = "let";
  private static final String AS = "AS";
  private static final String MATCH = "match";
  private static final String OLD = "old";
  private static final String PAR = "par";
  private static final String EXCLAMATION = "!";

  public ContractLibPrettyPrinter(Writer writer) {
    this.writer = writer;
    this.indentation = 0;
  }

  public void printContractLibAst(ContractLibAst ast) {
    printList(ast.abstractions(), () -> {
      printNewLine();
      printNewLine();
    }, this::printAbstraction);
    printNewLine();
    printNewLine();
    printList(ast.contracts(), () -> {
      printNewLine();
      printNewLine();
    }, this::printContract);
  }

  public void printAbstraction(Abstraction abstraction) {
    printBlockMultilineLabeled(
        () -> print(DECLARE_ABSTRACTIONS),
        () -> {
          printBlockMultiline(() -> {
            printIndentation();
            printSortDecDef(abstraction.identifier());
          });
          printNewLine();
          printBlockMultiline(() -> {
            printBlockMultiline(() -> {
              printListNewLine(abstraction.datatypeDec().constructors(), this::printConstructor);
            });
          });
        });
  }

  public void printSymbol(Symbol symbol) {
    print(symbol.identifier());
  }

  public void printNumeral(Numeral numeral) {
    print(numeral.value());
  }

  public void printSort(Sort sort) {
    sort.perform(
        this::printSortType,
        this::printSortParametricType);
  }

  public Void printSortType(Type sorttype) {
    print(sorttype.name());
    return null;
  }

  public Void printSortParametricType(ParametricType sorttype) {
    printBlockInline(() -> {
      print(sorttype.name());
      print(SPACE);
      printList(sorttype.arguments(), () -> print(SPACE), this::printSort);
    });
    return null;
  }

  public void printSortDecDef(SortDec def) {
    printBlockInline(() -> {
      printSymbol(def.name());
      print(SPACE);
      printNumeral(def.rank());
    });
  }

  public void printParameter(Parameter def) {
    printBlockInline(() -> {
      printSymbol(def.name());
    });
  }

  public void printConstructor(Constructor constructor) {
    printBlockMultilineLabeled(
        () -> printSymbol(constructor.identifier()),
        () -> {
          printIndentation();
          printListNewLine(constructor.selectors(), this::printSelector);
        });
  }

  public void printSelector(SelectorDec selectorDec) {
    printBlockInline(() -> {
      printSymbol(selectorDec.symbol());
      print(SPACE);
      printSort(selectorDec.sort());
    });
  }

  public void printContract(Contract contract) {
    printBlockMultilineLabeled(
        () -> {
          print(DEFINE_CONTRACT);
          print(SPACE);
          printSymbol(contract.identifier());
        },
        () -> {
          printBlockMultiline(() -> {
            printIndentation();
            printListNewLine(contract.formals(), this::printFormal);
          });
          printNewLine();
          printBlockMultiline(() -> {
            printListNewLine(contract.pairs(), this::printPrePostPair);
          });
        });
  }

  public void printPrePostPair(PrePostPair pair) {
    printBlockMultiline(() -> {
      printIndentation();
      printTerm(pair.pre());
      printNewLine();
      printIndentation();
      printTerm(pair.post());
    });
  }

  public void printFormal(Formal formal) {
    printBlockInline(() -> {
      printSymbol(formal.identifier());
      print(SPACE);
      printBlockInline(() -> {
        printArgumentMode(formal.argumentMode());
        print(SPACE);
        printSort(formal.sort());
      });
    });
  }

  public void printArgumentMode(ArgumentMode argumentMode) {
    switch (argumentMode) {
      case ArgumentMode.IN -> print(ARGUMENT_IN);
      case ArgumentMode.OUT -> print(ARGUMENT_OUT);
      case ArgumentMode.INOUT -> print(ARGUMENT_INOUT);
    }
  }

  public void printTerm(Term term) {
    term.perform(
        this::printSpecificConstant,
        this::printTermIdentifierAs,
        this::printTermIdentifierValue,
        this::printMethodApplication,
        this::printOld,
        this::printBooleanLiteral,
        this::printNumberLiteral,
        this::printLetBinding,
        this::printQuantorBinding,
        this::printMatchBinding,
        this::printAttributes);

  }

  public Void printSpecificConstant(SpecConstant specConstant) {
    print(specConstant.value());
    return null;
  }

  public Void printTermIdentifierAs(IdentifierAs identifierAs) {
    printBlockInline(() -> {
      printTermIdentifierValue(identifierAs.identifier());
      print(SPACE);
      print(AS);
      print(SPACE);
      printSort(identifierAs.sort());
    });
    return null;
  }

  public Void printTermIdentifierValue(IdentifierValue identifierValue) {
    printSymbol(identifierValue.identifier());
    return null;
  }

  public Void printMethodApplication(MethodApplication methodApplication) {
    printBlockInline(() -> {
      printTerm(methodApplication.identifier());
      print(SPACE);
      printList(methodApplication.parameters(), () -> print(SPACE), this::printTerm);
    });
    return null;
  }

  public Void printOld(Old old) {
    printBlockInline(() -> {
      print(OLD);
      print(SPACE);
      printTerm(old.argument());
    });
    return null;
  }

  public Void printBooleanLiteral(BooleanLiteral booleanLiteral) {
    print(Boolean.toString(booleanLiteral.value()));
    return null;
  }

  public Void printNumberLiteral(NumberLiteral numberLiteral) {
    print(numberLiteral.value());
    return null;
  }

  public Void printLetBinding(LetBinding letBinding) {

    printBlockInline(() -> {
      print(LET);
      print(SPACE);
      printBlockInline(() -> {
        printListNewLine(letBinding.varbindings(), this::printVarBinding);
      });
      printTerm(letBinding.term());
    });

    return null;
  }

  public void printQuantor(Quantor quantor) {
    switch (quantor) {
      case Quantor.EXISTS -> print(QUANTOR_EXISTS);
      case Quantor.ALL -> print(QUANTOR_ALL);
    }
  }

  public Void printQuantorBinding(QuantorBinding quantorBinding) {
    printBlockInline(() -> {
      printQuantor(quantorBinding.quantor());
      print(SPACE);
      printBlockInline(() -> {
        printList(quantorBinding.formals(), () -> print(SPACE), this::printSortedVar);
      });
      print(SPACE);
      printTerm(quantorBinding.term());
    });
    return null;
  }

  public Void printVarBinding(VarBinding varBinding) {
    printBlockInline(() -> {
      printSymbol(varBinding.name());
      print(SPACE);
      printTerm(varBinding.type());
    });

    return null;
  }

  public Void printMatchBinding(MatchBinding matchBinding) {
    printBlockInline(() -> {
      print(MATCH);
      print(SPACE);
      printTerm(matchBinding.term());
      printBlockInline(() -> {
        printListNewLine(matchBinding.matchCases(), this::printMatchCase);
      });

    });

    return null;
  }

  public void printPattern(Pattern pattern) {
    pattern.perform(this::printPatternCase, this::printPatternWithParameters);
  }

  public Void printPatternCase(Case patternCase) {
    printSymbol(patternCase.symbol());
    return null;
  }

  public Void printPatternWithParameters(WithParameters withParameters) {
    printBlockInline(() -> {
      printSymbol(withParameters.symbol());
      print(SPACE);
      printList(withParameters.parameters(), () -> print(SPACE), this::printSymbol);
    });
    return null;
  }

  public void printMatchCase(MatchCase matchCase) {
    printBlockInline(() -> {
      printPattern(matchCase.pattern());
      print(SPACE);
      printTerm(matchCase.term());
    });
  }

  public Void printAttributes(Attributes attributes) {
    printBlockInline(() -> {
      printTerm(attributes.term());
      print(EXCLAMATION);
      print(attributes.attributes().stream().collect(Collectors.joining(SPACE)));
    });

    return null;
  }

  public void printSortedVar(SortedVar sortedVar) {
    printBlockInline(() -> {
      printSymbol(sortedVar.symbol());
      print(SPACE);
      printSort(sortedVar.sort());
    });
  }

  public void printNewLine() {
    print(System.lineSeparator());
  }

  // - Helper

  @FunctionalInterface
  private interface BlockContent {
    void print();
  }

  @FunctionalInterface
  private interface Separator {
    void print();
  }

  private void newLineSeparator() {
    printNewLine();
    printIndentation();
  }

  private <T> void printListNewLine(List<T> print, Consumer<T> consumer) {
    printList(print, this::newLineSeparator, consumer);
  }

  private <T> void printList(List<T> print, Separator separator, Consumer<T> consumer) {
    Iterator<T> i = print.iterator();
    if (!i.hasNext()) {
      return;
    }
    T t = i.next();
    consumer.accept(t);

    while (i.hasNext()) {
      t = i.next();
      separator.print();
      consumer.accept(t);
    }
  }

  private void print(String text) {
    try {
      this.writer.write(text);
    } catch (IOException e) {
      //TODO: Add error handling
    }
  }

  private void printBlockMultilineLabeled(BlockContent label, BlockContent bc) {
    printIndentation();
    print(BRACKET_OPEN);
    label.print();
    printNewLine();
    increaseIndentation();
    bc.print();
    decreaseIndentation();
    printNewLine();
    printIndentation();
    print(BRACKET_CLOSE);
  }

  private void printBlockMultiline(BlockContent bc) {
    printIndentation();
    print(BRACKET_OPEN);
    printNewLine();
    increaseIndentation();
    bc.print();
    decreaseIndentation();
    printNewLine();
    printIndentation();
    print(BRACKET_CLOSE);
  }

  private void printBlockInline(BlockContent bc) {
    print(BRACKET_OPEN);
    increaseIndentation();
    bc.print();
    decreaseIndentation();
    print(BRACKET_CLOSE);
  }

  private void increaseIndentation() {
    indentation += DEFAULT_INDENTATION;
  }

  private void decreaseIndentation() {
    indentation -= DEFAULT_INDENTATION;
  }

  private void printIndentation() {
    print(SPACE.repeat(indentation));
  }
}
