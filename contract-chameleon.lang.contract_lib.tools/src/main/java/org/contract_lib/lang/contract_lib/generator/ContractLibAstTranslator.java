package org.contract_lib.lang.contract_lib.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.contract_lib.lang.contract_lib.antlr4parser.ContractLIBParser;
import org.contract_lib.lang.contract_lib.ast.Abstraction;
import org.contract_lib.lang.contract_lib.ast.ArgumentMode;
import org.contract_lib.lang.contract_lib.ast.Assert;
import org.contract_lib.lang.contract_lib.ast.Constant;
import org.contract_lib.lang.contract_lib.ast.Constructor;
import org.contract_lib.lang.contract_lib.ast.Contract;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.ast.Datatype;
import org.contract_lib.lang.contract_lib.ast.DatatypeDec;
import org.contract_lib.lang.contract_lib.ast.Formal;
import org.contract_lib.lang.contract_lib.ast.FunctionDec;
import org.contract_lib.lang.contract_lib.ast.MatchCase;
import org.contract_lib.lang.contract_lib.ast.Numeral;
import org.contract_lib.lang.contract_lib.ast.Pattern;
import org.contract_lib.lang.contract_lib.ast.PrePostPair;
import org.contract_lib.lang.contract_lib.ast.Quantor;
import org.contract_lib.lang.contract_lib.ast.SelectorDec;
import org.contract_lib.lang.contract_lib.ast.Sort;
import org.contract_lib.lang.contract_lib.ast.SortDec;
import org.contract_lib.lang.contract_lib.ast.SortedVar;
import org.contract_lib.lang.contract_lib.ast.Symbol;
import org.contract_lib.lang.contract_lib.ast.Term;
import org.contract_lib.lang.contract_lib.ast.VarBinding;

class ContractLibAstTranslator {

  @FunctionalInterface
  private interface ExtensionHelper<T, C> {
    void accept(ContractLibAstTranslatorExtension e, T res, C context);
  }

  private static final Numeral DEFAULT_RANK_DATATYPE_DECLARATION = new Numeral("0");

  private final List<ContractLibAstTranslatorExtension> extensions;

  ContractLibAstTranslator(final List<ContractLibAstTranslatorExtension> extensions) {
    this.extensions = extensions;
  }

  ContractLibAstTranslator() {
    this(new ArrayList<>());
  }

  // - MARK: Command Translations

  ContractLibAst translateStart(final ContractLIBParser.Start_Context ctx) {
    final List<Assert> asserts = new ArrayList<>();
    final List<Abstraction> abstractions = new ArrayList<>();
    final List<Datatype> datatypes = new ArrayList<>();
    final List<SortDec.Def> sorts = new ArrayList<>();
    final List<SortDec.Parameter> sortParameterss = new ArrayList<>();
    final List<FunctionDec> functions = new ArrayList<>();
    final List<Constant> constants = new ArrayList<>();
    final List<Contract> contracts = new ArrayList<>();

    for (final ContractLIBParser.CommandContext command : ctx.script().command()) {
      // Declare Commands
      testAndAppend(asserts, command.cmd_assert(), this::translateAssert);
      testAndAppend(abstractions, command.cmd_declareAbstraction(), this::translateDeclareAbstraction);
      testAndAppendStream(abstractions, command.cmd_declareAbstractions(), this::translateDeclareAbstractions);
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

    //TODO: To implement
    final ContractLibAst ast = new ContractLibAst(
        datatypes,
        abstractions,
        sorts,
        sortParameterss,
        functions,
        constants,
        contracts,
        asserts);

    callExtensions(ast, ctx, ContractLibAstTranslatorExtension::extendsionContractLibAst);

    return ast;
  }

  Assert translateAssert(final ContractLIBParser.Cmd_assertContext ctx) {
    final Term term = translateTerm(ctx.term());
    final Assert assertV = new Assert(term);
    return assertV;
  }

  Abstraction translateDeclareAbstraction(final ContractLIBParser.Cmd_declareAbstractionContext ctx) {
    final Symbol symbol = translateSymbol(ctx.symbol());
    final SortDec.Def sort = new SortDec.Def(symbol, DEFAULT_RANK_DATATYPE_DECLARATION);

    //TODO: Problem in how the datatypes of the abstraction are named? 
    final SortDec.Def missingName = new SortDec.Def(new Symbol("missing_type_symbol"),
        DEFAULT_RANK_DATATYPE_DECLARATION);

    final DatatypeDec datatypeDec = this.translateDatatypeDec(ctx.datatype_dec());

    final Abstraction abstraction = new Abstraction(
        sort,
        datatypeDec);

    //TODO: call extensions 

    return abstraction;
  }

  Stream<Abstraction> translateDeclareAbstractions(final ContractLIBParser.Cmd_declareAbstractionsContext ctx) {

    final List<SortDec.Def> sortDec = ctx.sort_dec().stream()
        .map(this::translateSortDec)
        .collect(Collectors.toList());

    final List<DatatypeDec> helpers = ctx.datatype_dec().stream()
        .map(this::translateDatatypeDec)
        .collect(Collectors.toList());

    if (sortDec.size() != helpers.size()) {
      //TODO: Handle error
    }

    final Stream<Abstraction> abstractions = IntStream.range(0, sortDec.size())
        .mapToObj(i -> new Abstraction(
            sortDec.get(i),
            helpers.get(i)));
    //TODO: call extensions 

    return abstractions;
  }

  Constant translateConstant(final ContractLIBParser.Cmd_declareConstContext ctx) {
    final Symbol symbol = translateSymbol(ctx.symbol());
    final Sort sort = translateSort(ctx.sort());

    final Constant constant = new Constant(symbol, sort);

    //TODO: call extensions 

    return constant;
  }

  Datatype translateDeclareDatatype(final ContractLIBParser.Cmd_declareDatatypeContext ctx) {
    final Symbol symbol = translateSymbol(ctx.symbol());
    final SortDec.Def sort = new SortDec.Def(symbol, DEFAULT_RANK_DATATYPE_DECLARATION);

    final DatatypeDec dtDec = translateDatatypeDec(ctx.datatype_dec());

    final Datatype datatype = new Datatype(
        sort,
        dtDec);
    //TODO: call extensions 

    return datatype;
  }

  Stream<Datatype> translateDeclareDatatypes(final ContractLIBParser.Cmd_declareDatatypesContext ctx) {

    //TODO: Think about how to handle the extension properly

    final List<SortDec.Def> sortDec = ctx.sort_dec().stream()
        .map(this::translateSortDec)
        .collect(Collectors.toList());

    final List<DatatypeDec> helpers = ctx.datatype_dec().stream()
        .map(this::translateDatatypeDec)
        .collect(Collectors.toList());

    if (sortDec.size() != helpers.size()) {
      //TODO: Handle error
    }

    final Stream<Datatype> datatypes = IntStream.range(0, sortDec.size())
        .mapToObj(i -> new Datatype(
            sortDec.get(i),
            helpers.get(i)));

    //TODO: call extensions 

    return datatypes;
  }

  FunctionDec translateDeclareFun(final ContractLIBParser.Cmd_declareFunContext ctx) {
    final Symbol symbol = translateSymbol(ctx.symbol());

    final List<Sort> sorts = ctx.sort().stream()
        .map(this::translateSort)
        .collect(Collectors.toList());

    final Sort result = sorts.removeLast();
    final List<Sort> arguments = sorts;

    final FunctionDec function = new FunctionDec(
        symbol,
        new ArrayList(),
        arguments,
        result);
    //TODO: call extensions 
    return null;
  }

  SortDec.Def translateDeclareSort(final ContractLIBParser.Cmd_declareSortContext ctx) {
    final Symbol symbol = translateSymbol(ctx.symbol());
    final Numeral numeral = translateNumeral(ctx.numeral());

    //TODO: Do better error handling than null-checks, rework interface to Option??
    //if (symbol == null | numeral == null) {
    if (numeral == null) {
      return null;
    }
    final SortDec.Def sort = new SortDec.Def(symbol, numeral);

    //TODO: call extensions 

    return sort;
  }

  FunctionDec translateDefineFun(final ContractLIBParser.Cmd_defineFunContext ctx) {
    //TODO: Implement / other type!!
    return null;
  }

  FunctionDec translateDefineFunRec(final ContractLIBParser.Cmd_defineFunRecContext ctx) {
    //TODO: Implement / other type!!
    //TODO: Implement
    return null;
  }

  FunctionDec translateDefineFunsRec(final ContractLIBParser.Cmd_defineFunsRecContext ctx) {
    //TODO: Implement / other type!!
    //TODO: Implement
    return null;
  }

  SortDec translateDefineSort(final ContractLIBParser.Cmd_defineSortContext ctx) {
    //TODO: Implement / other type necessary??
    return null;
  }

  Formal translateFormal(final ContractLIBParser.FormalContext ctx) {
    final ArgumentMode argumentMode = translateArgumentMode(ctx.argument_mode());//TODO: Implement
    final Sort sort = translateSort(ctx.sort());
    final Symbol symbol = translateSymbol(ctx.symbol());

    final Formal formal = new Formal(
        symbol,
        argumentMode,
        sort);

    //TODO: Implement
    return formal;
  }

  PrePostPair translatePrePostPair(final ContractLIBParser.ContractContext ctx) {
    final Term pre = translateTerm(ctx.term().get(0));
    final Term post = translateTerm(ctx.term().get(1));
    final PrePostPair prePost = new PrePostPair(
        pre,
        post);
    //TODO: Implement
    return prePost;
  }

  // - MARK: Translations

  Contract translateDefineContract(final ContractLIBParser.Cmd_defineContractContext ctx) {
    final Symbol symbol = translateSymbol(ctx.symbol());
    final List<Formal> formals = ctx.formal().stream()
        .map(this::translateFormal)
        .collect(Collectors.toList());

    final List<PrePostPair> pairs = ctx.contract().stream()
        .map(this::translatePrePostPair)
        .collect(Collectors.toList());

    final Contract contract = new Contract(
        symbol,
        formals,
        pairs);

    //TODO: Implement
    return contract;
  }

  SortDec.Def translateSortDec(final ContractLIBParser.Sort_decContext ctx) {

    final Symbol symbol = translateSymbol(ctx.symbol());
    final Numeral numeral = translateNumeral(ctx.numeral());

    final SortDec.Def sortDec = new SortDec.Def(
        symbol,
        numeral);

    //TODO: call extensions? 

    return sortDec;
  }

  SelectorDec translateSelectorDec(final ContractLIBParser.Selector_decContext ctx) {

    final Symbol symbol = translateSymbol(ctx.symbol());

    final Sort def = translateSort(ctx.sort());

    //TODO: call extensions? 

    final SelectorDec selector = new SelectorDec(
        symbol,
        def);

    return selector;
  }

  Constructor translateConstructor(final ContractLIBParser.Constructor_decContext ctx) {
    final Symbol symbol = translateSymbol(ctx.symbol());
    final List<SelectorDec> selectors = ctx.selector_dec().stream()
        .map(this::translateSelectorDec)
        .collect(Collectors.toList());

    final Constructor constructor = new Constructor(
        symbol,
        selectors);

    //TODO: call extensions?

    return constructor;
  }

  DatatypeDec translateDatatypeDec(final ContractLIBParser.Datatype_decContext ctx) {
    final List<SortDec.Parameter> par = new ArrayList<>();

    if (ctx.GRW_Par() != null) {
      ctx.symbol().stream()
          .map(this::translateSymbol)
          .map(SortDec.Parameter::new)
          .collect(Collectors.toList());
    }

    final List<Constructor> constructors = ctx.constructor_dec().stream()
        .map(this::translateConstructor)
        .collect(Collectors.toList());

    final DatatypeDec helper = new DatatypeDec(
        par,
        constructors);

    return helper;
  }

  Term translateTerm(final ContractLIBParser.TermContext ctx) {
    Term term = null;

    if (ctx.spec_constant() != null) {
      term = translateSpecConstant(ctx.spec_constant());
    } else if (ctx.qual_identifer() != null) {

      final Term.Identifier identifier = translateQualIdentifier(ctx.qual_identifer());

      if (ctx.term().size() > 0) {
        final List<Term> parameters = ctx.term().stream()
            .map(this::translateTerm)
            .collect(Collectors.toList());
        term = new Term.MethodApplication(
            identifier,
            parameters);
      } else {
        term = checkForBoolean(identifier);
      }

    } else if (ctx.GRW_Let() != null) {
      final List<VarBinding> vars = ctx.var_binding().stream()
          .map(this::translateVarBinding)
          .collect(Collectors.toList());
      final Term body = translateTerm(ctx.term(0));
      term = new Term.LetBinding(vars, body);

    } else if (ctx.GRW_Forall() != null) {
      final List<SortedVar> vars = ctx.sorted_var().stream()
          .map(this::translateSortedVar)
          .collect(Collectors.toList());
      final Term body = translateTerm(ctx.term(0));
      term = new Term.QuantorBinding(Quantor.ALL, vars, body);

    } else if (ctx.GRW_Exists() != null) {
      final List<SortedVar> vars = ctx.sorted_var().stream()
          .map(this::translateSortedVar)
          .collect(Collectors.toList());
      final Term body = translateTerm(ctx.term(0));
      term = new Term.QuantorBinding(Quantor.EXISTS, vars, body);

    } else if (ctx.GRW_Match() != null) {
      final List<MatchCase> matchCases = ctx.match_case().stream()
          .map(this::translateMatchCase)
          .collect(Collectors.toList());
      final Term body = translateTerm(ctx.term(0));
      term = new Term.MatchBinding(body, matchCases);

    } else if (ctx.GRW_Exclamation() != null) {
      final Term body = translateTerm(ctx.term(0));
      final List<String> attributes = ctx.attribute()
          .stream()
          .map(this::translateAttribute)
          .collect(Collectors.toList());
      term = new Term.Attributes(body, attributes);

    } else if (ctx.GRW_Old() != null) {
      final Term body = translateTerm(ctx.term(0));

      term = new Term.Old(body);
    }

    callExtensions(term, ctx, ContractLibAstTranslatorExtension::extendsionTerm);
    return term;
    //TODO: Allow parameters
  }

  Term checkForBoolean(final Term.Identifier id) {
    if (id.getValue().identifier().identifier().equals("true")) {
      return new Term.BooleanLiteral(true);
    } else if (id.getValue().identifier().identifier().equals("false")) {
      return new Term.BooleanLiteral(false);
    }
    return id;
  }

  MatchCase translateMatchCase(final ContractLIBParser.Match_caseContext ctx) {
    final Pattern pattern = translatePattern(ctx.pattern());
    final Term body = translateTerm(ctx.term());
    //TODO: call extensions
    return new MatchCase(pattern, body);
  }

  Pattern translatePattern(final ContractLIBParser.PatternContext ctx) {
    final List<Symbol> symbols = ctx.symbol().stream()
        .map(this::translateSymbol)
        .collect(Collectors.toList());

    final Symbol symbol = symbols.get(0);

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

  VarBinding translateVarBinding(final ContractLIBParser.Var_bindingContext ctx) {
    final Symbol symbol = translateSymbol(ctx.symbol());
    final Term term = translateTerm(ctx.term());

    final VarBinding binding = new VarBinding(
        symbol,
        term);
    //TODO: call extensions

    return binding;
  }

  SortedVar translateSortedVar(final ContractLIBParser.Sorted_varContext ctx) {
    final Symbol symbol = translateSymbol(ctx.symbol());
    final Sort sort = translateSort(ctx.sort());

    final SortedVar sortedVar = new SortedVar(
        symbol,
        sort);
    //TODO: call extensions

    return sortedVar;
  }

  Term translateSpecConstant(final ContractLIBParser.Spec_constantContext ctx) {

    if (ctx.numeral() != null |
        ctx.decimal() != null |
        ctx.hexadecimal() != null |
        ctx.binary() != null) {
      return new Term.NumberLiteral(ctx.getText());
    }
    //TODO: call extensions
    return new Term.SpecConstant(ctx.getText());
  }

  Term.Identifier.IdentifierValue translateIdentifier(final ContractLIBParser.IdentifierContext ctx) {
    final Symbol s = translateSymbol(ctx.symbol());
    if (ctx.LPAR() != null) {

    }
    //TODO: Implement
    return new Term.Identifier.IdentifierValue(s);
  }

  Term.Identifier translateQualIdentifier(final ContractLIBParser.Qual_identiferContext ctx) {
    final Term.Identifier.IdentifierValue v = translateIdentifier(ctx.identifier());
    if (ctx.LPAR() != null && ctx.GRW_As() != null) {
      final Sort s = translateSort(ctx.sort());
      //TODO: Call extensions
      return new Term.Identifier.IdentifierAs(v, s);
    }
    //TODO: Call extensions
    return v;
  }

  Sort translateSort(final ContractLIBParser.SortContext ctx) {
    //TODO: do proper parsing
    final String name = ctx.identifier().getText();
    if (ctx.LPAR() != null) {
      final List<Sort> parameters = ctx.sort()
          .stream()
          .map(this::translateSort)
          .collect(Collectors.toList());
      return new Sort.ParametricType(
          name,
          parameters);
    }

    return new Sort.Type(name);
  }

  Symbol translateSymbol(final ContractLIBParser.SymbolContext ctx) {
    final String value = ctx.getText();
    if (value.isEmpty()) {
      return null;
    }
    return new Symbol(value);
  }

  Numeral translateNumeral(final ContractLIBParser.NumeralContext ctx) {
    final String value = ctx.getText();
    if (value.isEmpty()) {
      return null;
    }
    return new Numeral(value);
  }

  String translateAttribute(final ContractLIBParser.AttributeContext ctx) {
    final String value = ctx.getText();
    if (value.isEmpty()) {
      return null;
    }
    return value;
  }

  // - MARK: Helper Methods

  ArgumentMode translateArgumentMode(final ContractLIBParser.Argument_modeContext ctx) {
    final String value = ctx.getText();
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

  private <C, T> void testAndAppendStream(
      final List<T> store,
      final C field,
      final java.util.function.Function<C, Stream<T>> handler) {
    if (field != null) {
      handler.apply(field).filter(Objects::nonNull).forEach(store::add);
    }
  }

  private <C, T> void testAndAppend(
      final List<T> store,
      final C field,
      final java.util.function.Function<C, T> handler) {
    if (field != null) {
      final T res = handler.apply(field);
      if (res != null) {
        store.add(res);
      }
    }
  }

  private <R, C> void callExtensions(
      final R result,
      final C context,
      final ExtensionHelper<R, C> handler) {
    for (final ContractLibAstTranslatorExtension e : this.extensions) {
      handler.accept(e, result, context);
    }
  }
}
