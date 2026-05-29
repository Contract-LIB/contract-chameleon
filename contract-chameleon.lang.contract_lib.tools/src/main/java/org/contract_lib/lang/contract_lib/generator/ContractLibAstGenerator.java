package org.contract_lib.lang.contract_lib.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
import org.contract_lib.lang.contract_lib.ast.JoinedCommand;
import org.contract_lib.lang.contract_lib.ast.MatchCase;
import org.contract_lib.lang.contract_lib.ast.Numeral;
import org.contract_lib.lang.contract_lib.ast.Parameter;
import org.contract_lib.lang.contract_lib.ast.Pattern;
import org.contract_lib.lang.contract_lib.ast.PrePostPair;
import org.contract_lib.lang.contract_lib.ast.Quantor;
import org.contract_lib.lang.contract_lib.ast.SelectorDec;
import org.contract_lib.lang.contract_lib.ast.Sort;
import org.contract_lib.lang.contract_lib.ast.Sort.ParametricType;
import org.contract_lib.lang.contract_lib.ast.Sort.Type;
import org.contract_lib.lang.contract_lib.ast.SortDec;
import org.contract_lib.lang.contract_lib.ast.SortedVar;
import org.contract_lib.lang.contract_lib.ast.Symbol;
import org.contract_lib.lang.contract_lib.ast.Term;
import org.contract_lib.lang.contract_lib.ast.Term.Identifier.IdentifierAs;
import org.contract_lib.lang.contract_lib.ast.Term.Identifier.IdentifierValue;
import org.contract_lib.lang.contract_lib.ast.Term.BooleanLiteral;
import org.contract_lib.lang.contract_lib.ast.Term.Identifier;
import org.contract_lib.lang.contract_lib.ast.Term.LetBinding;
import org.contract_lib.lang.contract_lib.ast.Term.MatchBinding;
import org.contract_lib.lang.contract_lib.ast.Term.MethodApplication;
import org.contract_lib.lang.contract_lib.ast.Term.NumberLiteral;
import org.contract_lib.lang.contract_lib.ast.Term.QuantorBinding;
import org.contract_lib.lang.contract_lib.ast.Term.SpecConstant;
import org.contract_lib.lang.contract_lib.ast.VarBinding;

public final class ContractLibAstGenerator {

  //TODO: Split up translation in different sections
  // - BaseGenerator (shared generation between other generators)
  // - CommandGenerator (Commands)
  // - SortGenerator (Dec & Def of Sort / Datatype / Abstraction)
  // - FuncGenerator (Dec & Def of functions)
  // - TermGenerator (Terms)
  // AstTranslator -> AstGenerator
  // - same for TranslatorExtenstion

  @FunctionalInterface
  private static interface ExtensionHelper<T, C> {
    void accept(ContractLibAstTranslatorExtension e, T res, C context);
  }

  private static final Numeral DEFAULT_RANK_DATATYPE_DECLARATION = new Numeral("0");

  private final List<ContractLibAstTranslatorExtension> extensions;

  public ContractLibAstGenerator(final List<ContractLibAstTranslatorExtension> extensions) {
    this.extensions = extensions;
  }

  public ContractLibAstGenerator() {
    this(new ArrayList<>());
  }

  // - MARK: Command Translations

  public final ContractLibAst translateStart(final ContractLIBParser.Start_Context ctx) {
    final List<Assert> asserts = new ArrayList<>();
    final List<Abstraction> abstractions = new ArrayList<>();
    final List<Datatype> datatypes = new ArrayList<>();
    final List<SortDec> sorts = new ArrayList<>();
    final List<Parameter> sortParameterss = new ArrayList<>();
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

    final ContractLibAst ast = new ContractLibAst(
        datatypes,
        abstractions,
        sorts,
        sortParameterss,
        functions,
        constants,
        contracts,
        asserts);

    callExtensions(ast, ctx, ContractLibAstTranslatorExtension::extensionContractLibAst);

    return ast;
  }

  Assert translateAssert(final ContractLIBParser.Cmd_assertContext ctx) {
    final Term term = translateTerm(ctx.term());
    final Assert assertV = new Assert(term);

    callExtensions(assertV, ctx, ContractLibAstTranslatorExtension::extensionAssert);

    return assertV;
  }

  Abstraction translateDeclareAbstraction(final ContractLIBParser.Cmd_declareAbstractionContext ctx) {
    final Symbol symbol = translateSymbol(ctx.symbol());
    final SortDec sort = new SortDec(symbol, DEFAULT_RANK_DATATYPE_DECLARATION);

    final DatatypeDec datatypeDec = this.translateDatatypeDec(ctx.datatype_dec());

    final Abstraction abstraction = new Abstraction(
        sort,
        datatypeDec);

    callExtensions(abstraction, ctx, ContractLibAstTranslatorExtension::extensionCmdDeclareAbstraction);

    return abstraction;
  }

  List<Abstraction> translateDeclareAbstractions(final ContractLIBParser.Cmd_declareAbstractionsContext ctx) {

    final List<SortDec> sortDec = ctx.sort_dec().stream()
        .map(this::translateSortDec)
        .collect(Collectors.toList());

    final List<DatatypeDec> helpers = ctx.datatype_dec().stream()
        .map(this::translateDatatypeDec)
        .collect(Collectors.toList());

    if (sortDec.size() != helpers.size()) {
      //TODO: Handle error
    }

    final List<Abstraction> abstractions = IntStream.range(0, sortDec.size())
        .mapToObj(i -> new Abstraction(
            sortDec.get(i),
            helpers.get(i)))
        .toList();

    JoinedCommand<Abstraction> joinedCommand = new JoinedCommand<>(abstractions);

    callExtensions(joinedCommand, ctx, ContractLibAstTranslatorExtension::extensionCmdDeclareAbstractions);

    return abstractions;
  }

  Constant translateConstant(final ContractLIBParser.Cmd_declareConstContext ctx) {
    final Symbol symbol = translateSymbol(ctx.symbol());
    final Sort sort = translateSort(ctx.sort());

    final Constant constant = new Constant(symbol, sort);

    callExtensions(constant, ctx, ContractLibAstTranslatorExtension::extensionCmdDeclareConstant);

    return constant;
  }

  Datatype translateDeclareDatatype(final ContractLIBParser.Cmd_declareDatatypeContext ctx) {
    final Symbol symbol = translateSymbol(ctx.symbol());
    final SortDec sort = new SortDec(symbol, DEFAULT_RANK_DATATYPE_DECLARATION);

    final DatatypeDec dtDec = translateDatatypeDec(ctx.datatype_dec());

    final Datatype datatype = new Datatype(
        sort,
        dtDec);

    callExtensions(datatype, ctx, ContractLibAstTranslatorExtension::extensionCmdDeclareDatatype);

    return datatype;
  }

  List<Datatype> translateDeclareDatatypes(final ContractLIBParser.Cmd_declareDatatypesContext ctx) {

    final List<SortDec> sortDec = ctx.sort_dec().stream()
        .map(this::translateSortDec)
        .collect(Collectors.toList());

    final List<DatatypeDec> helpers = ctx.datatype_dec().stream()
        .map(this::translateDatatypeDec)
        .collect(Collectors.toList());

    if (sortDec.size() != helpers.size()) {
      //TODO: Handle error
    }

    final List<Datatype> datatypes = IntStream.range(0, sortDec.size())
        .mapToObj(i -> new Datatype(
            sortDec.get(i),
            helpers.get(i)))
        .toList();

    JoinedCommand<Datatype> joinedCommand = new JoinedCommand<>(datatypes);

    callExtensions(joinedCommand, ctx, ContractLibAstTranslatorExtension::extensionCmdDeclareDatatypes);

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
        new ArrayList<>(),
        arguments,
        result);

    callExtensions(function, ctx, ContractLibAstTranslatorExtension::extensionCmdDeclareFun);

    return function;
  }

  SortDec translateDeclareSort(final ContractLIBParser.Cmd_declareSortContext ctx) {
    final Symbol symbol = translateSymbol(ctx.symbol());
    final Numeral numeral = translateNumeral(ctx.numeral());

    //TODO: Do better error handling than null-checks, rework interface to Option??
    //if (symbol == null | numeral == null) {
    if (numeral == null) {
      return null;
    }

    final SortDec sort = new SortDec(symbol, numeral);

    callExtensions(sort, ctx, ContractLibAstTranslatorExtension::extensionCmdDeclareSort);

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
    //TODO: Implement / other type necessary
    //TODO: Implement
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

    callExtensions(formal, ctx, ContractLibAstTranslatorExtension::extensionFormal);

    return formal;
  }

  // - MARK: Translations

  PrePostPair translatePrePostPair(final ContractLIBParser.ContractContext ctx) {
    final Term pre = translateTerm(ctx.term().get(0));
    final Term post = translateTerm(ctx.term().get(1));
    final PrePostPair prePost = new PrePostPair(
        pre,
        post);

    callExtensions(prePost, ctx, ContractLibAstTranslatorExtension::extensionPrePostPair);
    return prePost;
  }

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

    callExtensions(contract, ctx, ContractLibAstTranslatorExtension::extensionCmdDefineContract);

    return contract;
  }

  SortDec translateSortDec(final ContractLIBParser.Sort_decContext ctx) {

    final Symbol symbol = translateSymbol(ctx.symbol());
    final Numeral numeral = translateNumeral(ctx.numeral());

    final SortDec sortDec = new SortDec(
        symbol,
        numeral);

    callExtensions(sortDec, ctx, ContractLibAstTranslatorExtension::extensionSortDec);

    return sortDec;
  }

  SelectorDec translateSelectorDec(final ContractLIBParser.Selector_decContext ctx) {

    final Symbol symbol = translateSymbol(ctx.symbol());

    final Sort def = translateSort(ctx.sort());

    final SelectorDec selector = new SelectorDec(
        symbol,
        def);

    callExtensions(selector, ctx, ContractLibAstTranslatorExtension::extensionSelector);

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

    callExtensions(constructor, ctx, ContractLibAstTranslatorExtension::extensionConstructor);

    return constructor;
  }

  DatatypeDec translateDatatypeDec(final ContractLIBParser.Datatype_decContext ctx) {
    final List<Parameter> par = new ArrayList<>();

    if (ctx.GRW_Par() != null) {
      ctx.symbol().stream()
          .map(this::translateSymbol)
          .map(Parameter::new)
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
    if (ctx.spec_constant() != null) {
      return translateSpecConstant(ctx.spec_constant());

    } else if (ctx.qual_identifer() != null) {
      final Identifier identifier = translateQualIdentifier(ctx.qual_identifer());
      if (ctx.term().size() > 0) {
        final List<Term> parameters = ctx.term().stream()
            .map(this::translateTerm)
            .collect(Collectors.toList());
        MethodApplication method = new MethodApplication(
            identifier,
            parameters);
        callExtensions(method, ctx, ContractLibAstTranslatorExtension::extensionTermMethodApplication);
        return method;

      } else {
        return checkForBoolean(identifier, ctx);
      }

    } else if (ctx.GRW_Let() != null) {
      final List<VarBinding> vars = ctx.var_binding().stream()
          .map(this::translateVarBinding)
          .collect(Collectors.toList());
      final Term body = translateTerm(ctx.term(0));
      LetBinding letBinding = new Term.LetBinding(vars, body);
      callExtensions(letBinding, ctx, ContractLibAstTranslatorExtension::extensionTermLetBinding);
      return letBinding;

    } else if (ctx.GRW_Forall() != null) {
      final List<SortedVar> vars = ctx.sorted_var().stream()
          .map(this::translateSortedVar)
          .collect(Collectors.toList());
      final Term body = translateTerm(ctx.term(0));
      QuantorBinding quantorBinding = new Term.QuantorBinding(Quantor.ALL, vars, body);
      callExtensions(quantorBinding, ctx, ContractLibAstTranslatorExtension::extensionTermQuantorBinding);
      return quantorBinding;

    } else if (ctx.GRW_Exists() != null) {
      final List<SortedVar> vars = ctx.sorted_var().stream()
          .map(this::translateSortedVar)
          .collect(Collectors.toList());
      final Term body = translateTerm(ctx.term(0));
      QuantorBinding quantorBinding = new Term.QuantorBinding(Quantor.EXISTS, vars, body);
      callExtensions(quantorBinding, ctx, ContractLibAstTranslatorExtension::extensionTermQuantorBinding);
      return quantorBinding;

    } else if (ctx.GRW_Match() != null) {
      final List<MatchCase> matchCases = ctx.match_case().stream()
          .map(this::translateMatchCase)
          .collect(Collectors.toList());
      final Term body = translateTerm(ctx.term(0));
      MatchBinding matchBinding = new Term.MatchBinding(body, matchCases);
      callExtensions(matchBinding, ctx, ContractLibAstTranslatorExtension::extensionTermMatchBinding);
      return matchBinding;

    } else if (ctx.GRW_Exclamation() != null) {
      final Term body = translateTerm(ctx.term(0));
      final List<String> attributes = ctx.attribute()
          .stream()
          .map(this::translateAttribute)
          .collect(Collectors.toList());
      final Term.Attributes attr = new Term.Attributes(body, attributes);
      callExtensions(attr, ctx, ContractLibAstTranslatorExtension::extensionTermAttributes);
      return attr;

    } else if (ctx.GRW_Old() != null) {
      final Term body = translateTerm(ctx.term(0));
      final Term.Old old = new Term.Old(body);
      callExtensions(old, ctx, ContractLibAstTranslatorExtension::extensionTermOld);
      return old;
    }
    //NOTE: never reached, better matching?
    return null;
  }

  Term checkForBoolean(final Term.Identifier id, ContractLIBParser.TermContext ctx) {
    if (id.getValue().identifier().identifier().equals("true")) {

      BooleanLiteral booleanLiteral = new Term.BooleanLiteral(true);
      callExtensions(booleanLiteral, ctx, ContractLibAstTranslatorExtension::extendsionTermBooleanLiteral);
      return booleanLiteral;
    } else if (id.getValue().identifier().identifier().equals("false")) {
      return new Term.BooleanLiteral(false);
    }
    return id;
  }

  Term translateSpecConstant(final ContractLIBParser.Spec_constantContext ctx) {

    if (ctx.numeral() != null |
        ctx.decimal() != null |
        ctx.hexadecimal() != null |
        ctx.binary() != null) {

      final NumberLiteral numberLiteral = new NumberLiteral(ctx.getText());

      callExtensions(numberLiteral, ctx, ContractLibAstTranslatorExtension::extendsionTermNumberLiteral);
      return numberLiteral;
    }

    final SpecConstant specConstant = new Term.SpecConstant(ctx.getText());

    callExtensions(specConstant, ctx, ContractLibAstTranslatorExtension::extendsionTermSpecConstant);
    return specConstant;
  }

  IdentifierValue translateIdentifierValue(final ContractLIBParser.IdentifierContext ctx) {
    final Symbol s = translateSymbol(ctx.symbol());
    if (ctx.LPAR() != null) {
      //TODO: throw error!
    }
    final IdentifierValue identifierValue = new IdentifierValue(s);

    callExtensions(identifierValue, ctx, ContractLibAstTranslatorExtension::extendsionTermIdentifierValue);

    return identifierValue;
  }

  Identifier translateQualIdentifier(final ContractLIBParser.Qual_identiferContext ctx) {
    final IdentifierValue v = translateIdentifierValue(ctx.identifier());
    if (ctx.LPAR() != null && ctx.GRW_As() != null) {
      final Sort s = translateSort(ctx.sort());
      final IdentifierAs identifierAs = new Term.Identifier.IdentifierAs(v, s);

      callExtensions(identifierAs, ctx, ContractLibAstTranslatorExtension::extendsionTermIdentifierAs);
      return identifierAs;
    }
    return v;
  }

  MatchCase translateMatchCase(final ContractLIBParser.Match_caseContext ctx) {
    final Pattern pattern = translatePattern(ctx.pattern());
    final Term body = translateTerm(ctx.term());
    final MatchCase matchCase = new MatchCase(pattern, body);
    callExtensions(matchCase, ctx, ContractLibAstTranslatorExtension::extensionMatchCase);
    return matchCase;
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

    callExtensions(pattern, ctx, ContractLibAstTranslatorExtension::extensionPattern);

    return pattern;
  }

  VarBinding translateVarBinding(final ContractLIBParser.Var_bindingContext ctx) {
    final Symbol symbol = translateSymbol(ctx.symbol());
    final Term term = translateTerm(ctx.term());

    final VarBinding binding = new VarBinding(
        symbol,
        term);

    callExtensions(binding, ctx, ContractLibAstTranslatorExtension::extensionVarBinding);

    return binding;
  }

  SortedVar translateSortedVar(final ContractLIBParser.Sorted_varContext ctx) {
    final Symbol symbol = translateSymbol(ctx.symbol());
    final Sort sort = translateSort(ctx.sort());

    final SortedVar sortedVar = new SortedVar(
        symbol,
        sort);

    callExtensions(sortedVar, ctx, ContractLibAstTranslatorExtension::extensionSortedVar);

    return sortedVar;
  }

  Sort translateSort(final ContractLIBParser.SortContext ctx) {
    //TODO: do proper parsing
    final String name = ctx.identifier().getText();
    if (ctx.LPAR() != null) {
      final List<Sort> parameters = ctx.sort()
          .stream()
          .map(this::translateSort)
          .collect(Collectors.toList());

      final ParametricType parametricType = new Sort.ParametricType(
          name,
          parameters);

      callExtensions(parametricType, ctx, ContractLibAstTranslatorExtension::extensionSortParametricType);

      return parametricType;
    }

    final Type type = new Type(name);

    callExtensions(type, ctx, ContractLibAstTranslatorExtension::extensionSortType);
    return type;
  }

  @Deprecated
  Symbol translateSymbol(final ContractLIBParser.SymbolContext ctx) {
    final String value = ctx.getText();
    if (value.isEmpty()) {
      //TODO: throw error!
      return null;
    }
    return new Symbol(value);
  }

  Numeral translateNumeral(final ContractLIBParser.NumeralContext ctx) {
    final String value = ctx.getText();
    if (value.isEmpty()) {
      //TODO: throw error!
      return null;
    }
    final Numeral numeral = new Numeral(value);

    callExtensions(numeral, ctx, ContractLibAstTranslatorExtension::extensionNumeral);

    return numeral;
  }

  String translateAttribute(final ContractLIBParser.AttributeContext ctx) {

    // TODO: Add type
    final String value = ctx.getText();
    if (value.isEmpty()) {
      //TODO: throw error!
      return null;
    }

    callExtensions(value, ctx, ContractLibAstTranslatorExtension::extendsionAttribute);

    return value;
  }

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

  // - MARK: Helper Methods

  private <C, T> void testAndAppendStream(
      final List<T> store,
      final C field,
      final java.util.function.Function<C, List<T>> handler) {
    if (field != null) {
      handler.apply(field).stream().filter(Objects::nonNull).forEach(store::add);
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
