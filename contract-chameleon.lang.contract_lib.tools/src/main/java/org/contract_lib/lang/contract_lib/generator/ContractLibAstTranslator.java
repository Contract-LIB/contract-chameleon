package org.contract_lib.lang.contract_lib.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.contract_lib.contract_chameleon.error.ChameleonMessageManager;
import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser;

import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.ast.Abstraction;
import org.contract_lib.lang.contract_lib.ast.Assert;
import org.contract_lib.lang.contract_lib.ast.Constant;
import org.contract_lib.lang.contract_lib.ast.Constructor;
import org.contract_lib.lang.contract_lib.ast.Datatype;
import org.contract_lib.lang.contract_lib.ast.DatatypeDec;
import org.contract_lib.lang.contract_lib.ast.SortDec;
import org.contract_lib.lang.contract_lib.ast.FunctionDec;
import org.contract_lib.lang.contract_lib.ast.Contract;

import org.contract_lib.lang.contract_lib.ast.SelectorDec;
import org.contract_lib.lang.contract_lib.ast.Term;
import org.contract_lib.lang.contract_lib.ast.Sort;
import org.contract_lib.lang.contract_lib.ast.MatchCase;
import org.contract_lib.lang.contract_lib.ast.Pattern;
import org.contract_lib.lang.contract_lib.ast.Symbol;
import org.contract_lib.lang.contract_lib.ast.Numeral;
import org.contract_lib.lang.contract_lib.ast.Quantor;
import org.contract_lib.lang.contract_lib.ast.SortedVar;
import org.contract_lib.lang.contract_lib.ast.Formal;
import org.contract_lib.lang.contract_lib.ast.PrePostPair;
import org.contract_lib.lang.contract_lib.ast.ArgumentMode;
import org.contract_lib.lang.contract_lib.ast.VarBinding;
import org.contract_lib.lang.contract_lib.error.DimensionError;
import org.contract_lib.lang.contract_lib.label.AstTranslatorExtension;

class ContractLibAstTranslator {

  private final List<AstTranslatorExtension> extensions;
  private final ChameleonMessageManager messageManager;

  private static final Numeral DEFAULT_RANK_DATATYPE_DECLARATION = new Numeral("0");

  ContractLibAstTranslator(
      List<AstTranslatorExtension> extensions,
      ChameleonMessageManager messageManager) {

    this.messageManager = messageManager;
    this.extensions = new ArrayList<>(extensions);
  }

  ContractLibAstTranslator(ChameleonMessageManager messageManager) {
    this.extensions = new ArrayList<>();
    this.messageManager = messageManager;
  }

  ContractLibAst translateStart(ContractLIBParser.Start_Context ctx) {
    List<Assert> asserts = new ArrayList<>();
    List<Abstraction> abstractions = new ArrayList<>();
    List<Datatype> datatypes = new ArrayList<>();
    List<SortDec.Def> sorts = new ArrayList<>();
    List<SortDec.Parameter> sortParameterss = new ArrayList<>();
    List<FunctionDec> functions = new ArrayList<>();
    List<Constant> constants = new ArrayList<>();
    List<Contract> contracts = new ArrayList<>();

    for (ContractLIBParser.CommandContext command : ctx.script().command()) {
      // Declare Commands
      testAndAppend(asserts, command.cmd_assert(), this::translateAssert);
      testAndAppend(abstractions, command.cmd_declareAbstraction(), this::translateDeclareAbstraction);
      testAndAppendList(abstractions, command.cmd_declareAbstractions(), this::translateDeclareAbstractions);
      testAndAppend(constants, command.cmd_declareConst(), this::translateConstant);
      testAndAppend(datatypes, command.cmd_declareDatatype(), this::translateDeclareDatatype);
      testAndAppendStream(datatypes, command.cmd_declareDatatypes(), this::translateDeclareDatatypes);
      testAndAppend(functions, command.cmd_declareFun(), this::translateDeclareFun);
      testAndAppend(sorts, command.cmd_declareSort(), this::translateDeclareSort);
      // Define Commands
      testAndAppend(contracts, command.cmd_defineContract(), this::translateDefineContract);
      //TODO: To implement
      //testAndAppend(functions, command.cmd_defineFun(), this::translateDefineFun);
      //testAndAppend(functions, command.cmd_defineFunRec(), this::translateDefineFunRec);
      //testAndAppend(functions, command.cmd_defineFunsRec(), this::translateDefineFunsRec);
      //testAndAppend(sorts, command.cmd_defineSort(), this::translateDefineSort);
    }

    ContractLibAst ast = new ContractLibAst(
        datatypes,
        abstractions,
        sorts,
        sortParameterss,
        functions,
        constants,
        contracts,
        asserts);

    callExtensions(ast, ctx, AstTranslatorExtension::extensionContractLibAst);

    return ast;
  }

  // - MARK: Command Translations

  Assert translateAssert(ContractLIBParser.Cmd_assertContext ctx) {
    Term term = translateTerm(ctx.term());
    Assert assertV = new Assert(term);
    return assertV;
  }

  Abstraction translateDeclareAbstraction(ContractLIBParser.Cmd_declareAbstractionContext ctx) {
    Symbol symbol = translateSymbol(ctx.symbol());
    SortDec.Def sort = new SortDec.Def(symbol, DEFAULT_RANK_DATATYPE_DECLARATION);

    //TODO: Problem in how the datatypes of the abstraction are named? 
    SortDec.Def missingName = new SortDec.Def(new Symbol("missing_type_symbol"), DEFAULT_RANK_DATATYPE_DECLARATION);

    DatatypeDec datatypeDec = this.translateDatatypeDec(ctx.datatype_dec());

    Abstraction abstraction = new Abstraction(
        sort,
        datatypeDec);

    //callExtensions(abstraction, ctx, AstTranslatorExtension::extensionAbstraction);

    //TODO: call extensions 

    return abstraction;
  }

  List<Abstraction> translateDeclareAbstractions(ContractLIBParser.Cmd_declareAbstractionsContext ctx) {

    List<SortDec.Def> sortDec = ctx.sort_dec().stream()
        .map(this::translateSortDec)
        .collect(Collectors.toList());

    List<DatatypeDec> delarations = ctx.datatype_dec().stream()
        .map(this::translateDatatypeDec)
        .collect(Collectors.toList());

    int numberAbstractions = Integer.min(sortDec.size(), delarations.size());

    if (sortDec.size() != delarations.size()) {

      messageManager.report(new DimensionError(
          "", //TODO get file orgin
          ctx.start.getLine(),
          ctx.start.getCharPositionInLine(),
          String.format(
              "The dimensions do not match, given %d sort declarations and %d constructor definitions.",
              sortDec.size(),
              delarations.size())));
    }

    List<Abstraction> abstractions = IntStream.range(0, numberAbstractions)
        .mapToObj(i -> new Abstraction(
            sortDec.get(i),
            delarations.get(i)))
        .toList();

    callExtensions(abstractions, ctx, AstTranslatorExtension::extensionDeclareAbstractions);

    return abstractions;
  }

  Constant translateConstant(ContractLIBParser.Cmd_declareConstContext ctx) {
    Symbol symbol = translateSymbol(ctx.symbol());
    Sort sort = translateSort(ctx.sort());

    Constant constant = new Constant(symbol, sort);

    //TODO: call extensions 

    return constant;
  }

  Datatype translateDeclareDatatype(ContractLIBParser.Cmd_declareDatatypeContext ctx) {
    Symbol symbol = translateSymbol(ctx.symbol());
    SortDec.Def sort = new SortDec.Def(symbol, DEFAULT_RANK_DATATYPE_DECLARATION);

    DatatypeDec dtDec = translateDatatypeDec(ctx.datatype_dec());

    Datatype datatype = new Datatype(
        sort,
        dtDec);
    //TODO: call extensions 

    return datatype;
  }

  Stream<Datatype> translateDeclareDatatypes(ContractLIBParser.Cmd_declareDatatypesContext ctx) {

    //TODO: Think about how to handle the extension properly

    List<SortDec.Def> sortDec = ctx.sort_dec().stream()
        .map(this::translateSortDec)
        .collect(Collectors.toList());

    List<DatatypeDec> helpers = ctx.datatype_dec().stream()
        .map(this::translateDatatypeDec)
        .collect(Collectors.toList());

    if (sortDec.size() != helpers.size()) {
      //TODO: Handle error
    }

    Stream<Datatype> datatypes = IntStream.range(0, sortDec.size())
        .mapToObj(i -> new Datatype(
            sortDec.get(i),
            helpers.get(i)));

    //TODO: call extensions 

    return datatypes;
  }

  FunctionDec translateDeclareFun(ContractLIBParser.Cmd_declareFunContext ctx) {
    Symbol symbol = translateSymbol(ctx.symbol());

    List<Sort> sorts = ctx.sort().stream()
        .map(this::translateSort)
        .collect(Collectors.toList());

    Sort result = sorts.removeLast();
    List<Sort> arguments = sorts;

    FunctionDec function = new FunctionDec(
        symbol,
        new ArrayList(),
        arguments,
        result);
    //TODO: call extensions 
    return null;
  }

  SortDec.Def translateDeclareSort(ContractLIBParser.Cmd_declareSortContext ctx) {
    Symbol symbol = translateSymbol(ctx.symbol());
    Numeral numeral = translateNumeral(ctx.numeral());

    //TODO: Do better error handling than null-checks, rework interface to Option??
    //if (symbol == null | numeral == null) {
    if (numeral == null) {
      return null;
    }
    SortDec.Def sort = new SortDec.Def(symbol, numeral);

    //TODO: call extensions 

    return sort;
  }

  FunctionDec translateDefineFun(ContractLIBParser.Cmd_defineFunContext ctx) {
    //TODO: Implement / other type!!
    return null;
  }

  FunctionDec translateDefineFunRec(ContractLIBParser.Cmd_defineFunRecContext ctx) {
    //TODO: Implement / other type!!
    //TODO: Implement
    return null;
  }

  FunctionDec translateDefineFunsRec(ContractLIBParser.Cmd_defineFunsRecContext ctx) {
    //TODO: Implement / other type!!
    //TODO: Implement
    return null;
  }

  SortDec translateDefineSort(ContractLIBParser.Cmd_defineSortContext ctx) {
    //TODO: Implement / other type necessary??
    return null;
  }

  Formal translateFormal(ContractLIBParser.FormalContext ctx) {
    ArgumentMode argumentMode = translateArgumentMode(ctx.argument_mode());//TODO: Implement
    Sort sort = translateSort(ctx.sort());
    Symbol symbol = translateSymbol(ctx.symbol());

    Formal formal = new Formal(
        symbol,
        argumentMode,
        sort);

    //TODO: Implement
    return formal;
  }

  PrePostPair translatePrePostPair(ContractLIBParser.ContractContext ctx) {
    Term pre = translateTerm(ctx.term().get(0));
    Term post = translateTerm(ctx.term().get(1));
    PrePostPair prePost = new PrePostPair(
        pre,
        post);
    //TODO: Implement
    return prePost;
  }

  Contract translateDefineContract(ContractLIBParser.Cmd_defineContractContext ctx) {
    Symbol symbol = translateSymbol(ctx.symbol());
    List<Formal> formals = ctx.formal().stream()
        .map(this::translateFormal)
        .collect(Collectors.toList());

    List<PrePostPair> pairs = ctx.contract().stream()
        .map(this::translatePrePostPair)
        .collect(Collectors.toList());

    Contract contract = new Contract(
        symbol,
        formals,
        pairs);

    //TODO: Implement
    return contract;
  }

  // - MARK: Translations

  SortDec.Def translateSortDec(ContractLIBParser.Sort_decContext ctx) {

    Symbol symbol = translateSymbol(ctx.symbol());
    Numeral numeral = translateNumeral(ctx.numeral());

    SortDec.Def sortDec = new SortDec.Def(
        symbol,
        numeral);

    //TODO: call extensions? 

    return sortDec;
  }

  SelectorDec translateSelectorDec(ContractLIBParser.Selector_decContext ctx) {

    Symbol symbol = translateSymbol(ctx.symbol());

    Sort def = translateSort(ctx.sort());

    //TODO: call extensions? 

    SelectorDec selector = new SelectorDec(
        symbol,
        def);

    return selector;
  }

  Constructor translateConstructor(ContractLIBParser.Constructor_decContext ctx) {
    Symbol symbol = translateSymbol(ctx.symbol());
    List<SelectorDec> selectors = ctx.selector_dec().stream()
        .map(this::translateSelectorDec)
        .collect(Collectors.toList());

    Constructor constructor = new Constructor(
        symbol,
        selectors);

    //TODO: call extensions?

    return constructor;
  }

  DatatypeDec translateDatatypeDec(ContractLIBParser.Datatype_decContext ctx) {
    List<SortDec.Parameter> par = new ArrayList<>();

    if (ctx.GRW_Par() != null) {
      ctx.symbol().stream()
          .map(this::translateSymbol)
          .map(SortDec.Parameter::new)
          .collect(Collectors.toList());
    }

    List<Constructor> constructors = ctx.constructor_dec().stream()
        .map(this::translateConstructor)
        .collect(Collectors.toList());

    DatatypeDec helper = new DatatypeDec(
        par,
        constructors);

    return helper;
  }

  Term translateTerm(ContractLIBParser.TermContext ctx) {
    Term term = null;

    if (ctx.spec_constant() != null) {
      term = translateSpecConstant(ctx.spec_constant());
    } else if (ctx.qual_identifer() != null) {

      Term.Identifier identifier = translateQualIdentifier(ctx.qual_identifer());

      if (ctx.term().size() > 0) {
        List<Term> parameters = ctx.term().stream()
            .map(this::translateTerm)
            .collect(Collectors.toList());
        term = new Term.MethodApplication(
            identifier,
            parameters);
      } else {
        term = checkForBoolean(identifier);
      }

    } else if (ctx.GRW_Let() != null) {
      List<VarBinding> vars = ctx.var_binding().stream()
          .map(this::translateVarBinding)
          .collect(Collectors.toList());
      Term body = translateTerm(ctx.term(0));
      term = new Term.LetBinding(vars, body);

    } else if (ctx.GRW_Forall() != null) {
      List<SortedVar> vars = ctx.sorted_var().stream()
          .map(this::translateSortedVar)
          .collect(Collectors.toList());
      Term body = translateTerm(ctx.term(0));
      term = new Term.QuantorBinding(Quantor.ALL, vars, body);

    } else if (ctx.GRW_Exists() != null) {
      List<SortedVar> vars = ctx.sorted_var().stream()
          .map(this::translateSortedVar)
          .collect(Collectors.toList());
      Term body = translateTerm(ctx.term(0));
      term = new Term.QuantorBinding(Quantor.EXISTS, vars, body);

    } else if (ctx.GRW_Match() != null) {
      List<MatchCase> matchCases = ctx.match_case().stream()
          .map(this::translateMatchCase)
          .collect(Collectors.toList());
      Term body = translateTerm(ctx.term(0));
      term = new Term.MatchBinding(body, matchCases);

    } else if (ctx.GRW_Exclamation() != null) {
      Term body = translateTerm(ctx.term(0));
      List<String> attributes = ctx.attribute()
          .stream()
          .map(this::translateAttribute)
          .collect(Collectors.toList());
      term = new Term.Attributes(body, attributes);

    } else if (ctx.GRW_Old() != null) {
      Term body = translateTerm(ctx.term(0));

      term = new Term.Old(body);
    }

    callExtensions(term, ctx, AstTranslatorExtension::extensionTerm);

    return term;
  }

  Term checkForBoolean(Term.Identifier id) {
    if (id.getValue().identifier().identifier().equals("true")) {
      return new Term.BooleanLiteral(true);
    } else if (id.getValue().identifier().identifier().equals("false")) {
      return new Term.BooleanLiteral(false);
    }
    return id;
  }

  MatchCase translateMatchCase(ContractLIBParser.Match_caseContext ctx) {
    Pattern pattern = translatePattern(ctx.pattern());
    Term body = translateTerm(ctx.term());
    //TODO: call extensions
    return new MatchCase(pattern, body);
  }

  Pattern translatePattern(ContractLIBParser.PatternContext ctx) {
    List<Symbol> symbols = ctx.symbol().stream()
        .map(this::translateSymbol)
        .collect(Collectors.toList());

    Symbol symbol = symbols.get(0);

    Pattern pattern;

    if (symbols.size() == 1) {
      pattern = new Pattern.Case(symbol);
    } else {
      symbols.removeFirst();
      pattern = new Pattern.WithParameters(symbol, symbols);
    }

    //TODO: call extensions

    return pattern;
  }

  VarBinding translateVarBinding(ContractLIBParser.Var_bindingContext ctx) {
    Symbol symbol = translateSymbol(ctx.symbol());
    Term term = translateTerm(ctx.term());

    VarBinding binding = new VarBinding(
        symbol,
        term);
    //TODO: call extensions

    return binding;
  }

  SortedVar translateSortedVar(ContractLIBParser.Sorted_varContext ctx) {
    Symbol symbol = translateSymbol(ctx.symbol());
    Sort sort = translateSort(ctx.sort());

    SortedVar sortedVar = new SortedVar(
        symbol,
        sort);
    //TODO: call extensions

    return sortedVar;
  }

  Term translateSpecConstant(ContractLIBParser.Spec_constantContext ctx) {

    if (ctx.numeral() != null |
        ctx.decimal() != null |
        ctx.hexadecimal() != null |
        ctx.binary() != null) {
      return new Term.NumberLiteral(ctx.getText());
    }
    //TODO: call extensions
    return new Term.SpecConstant(ctx.getText());
  }

  Term.Identifier.IdentifierValue translateIdentifier(ContractLIBParser.IdentifierContext ctx) {
    Symbol s = translateSymbol(ctx.symbol());
    if (ctx.LPAR() != null) {

    }
    //TODO: Implement
    return new Term.Identifier.IdentifierValue(s);
  }

  Term.Identifier translateQualIdentifier(ContractLIBParser.Qual_identiferContext ctx) {
    Term.Identifier.IdentifierValue v = translateIdentifier(ctx.identifier());
    if (ctx.LPAR() != null && ctx.GRW_As() != null) {
      Sort s = translateSort(ctx.sort());
      //TODO: Call extensions
      return new Term.Identifier.IdentifierAs(v, s);
    }
    //TODO: Call extensions
    return v;
  }

  Sort translateSort(ContractLIBParser.SortContext ctx) {
    //TODO: do proper parsing
    String name = ctx.identifier().getText();
    if (ctx.LPAR() != null) {
      List<Sort> parameters = ctx.sort()
          .stream()
          .map(this::translateSort)
          .collect(Collectors.toList());
      return new Sort.ParametricType(
          name,
          parameters);
    }

    return new Sort.Type(name);
  }

  Symbol translateSymbol(ContractLIBParser.SymbolContext ctx) {
    String value = ctx.getText();
    if (value.isEmpty()) {
      return null;
    }
    return new Symbol(value);
  }

  Numeral translateNumeral(ContractLIBParser.NumeralContext ctx) {
    String value = ctx.getText();
    if (value.isEmpty()) {
      return null;
    }
    return new Numeral(value);
  }

  String translateAttribute(ContractLIBParser.AttributeContext ctx) {
    String value = ctx.getText();
    if (value.isEmpty()) {
      return null;
    }
    return value;
  }

  ArgumentMode translateArgumentMode(ContractLIBParser.Argument_modeContext ctx) {
    String value = ctx.getText();
    if (value.equals("in")) {
      return ArgumentMode.IN;
    } else if (value.equals("out")) {
      return ArgumentMode.OUT;
    } else if (value.equals("inout")) {
      return ArgumentMode.INOUT;
    } else {
      //TODO: handle error
      return null;
    }
  }

  // - MARK: Helper Methods
  private <C, T> void testAndAppendList(
      List<T> store,
      C field,
      java.util.function.Function<C, List<T>> handler) {
    if (field != null) {
      handler.apply(field).stream().filter(Objects::nonNull).forEach(store::add);
    }
  }

  private <C, T> void testAndAppendStream(
      List<T> store,
      C field,
      java.util.function.Function<C, Stream<T>> handler) {
    if (field != null) {
      handler.apply(field).filter(Objects::nonNull).forEach(store::add);
    }
  }

  private <C, T> void testAndAppend(
      List<T> store,
      C field,
      java.util.function.Function<C, T> handler) {
    if (field != null) {
      T res = handler.apply(field);
      if (res != null) {
        store.add(res);
      }
    }
  }

  private <R, C> void callExtensions(
      R result,
      C context,
      ExtensionHelper<R, C> handler) {
    for (AstTranslatorExtension e : this.extensions) {
      handler.accept(e, result, context);
    }
  }

  @FunctionalInterface
  private interface ExtensionHelper<T, C> {
    void accept(AstTranslatorExtension e, T res, C context);
  }
}
