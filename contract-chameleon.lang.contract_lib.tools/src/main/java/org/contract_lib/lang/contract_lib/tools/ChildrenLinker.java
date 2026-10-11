package org.contract_lib.lang.contract_lib.tools;

import java.util.List;
import java.util.stream.Stream;

import org.contract_lib.lang.contract_lib.ast.Abstraction;
import org.contract_lib.lang.contract_lib.ast.Command;
import org.contract_lib.lang.contract_lib.ast.ContractLibAst;
import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement;
import org.contract_lib.lang.contract_lib.ast.ContractLibAstElement.Inner;
import org.contract_lib.lang.contract_lib.ast.Datatype;
import org.contract_lib.lang.contract_lib.ast.JoinableCommand;
import org.contract_lib.lang.contract_lib.ast.JoinedCommand;
import org.contract_lib.lang.contract_lib.ast.Sort;
import org.contract_lib.lang.contract_lib.ast.Sort.ParametricType;
import org.contract_lib.lang.contract_lib.ast.Sort.Type;
import org.contract_lib.lang.contract_lib.ast.SortDec;
import org.contract_lib.lang.contract_lib.ast.Term;

public final class ChildrenLinker {

  public ChildrenLinker() {
  }

  /// Get all children nodes and the node itself.
  public Stream<? extends ContractLibAstElement> getAllNodes(ContractLibAstElement element) {
    Stream<? extends ContractLibAstElement> childeren = getAllChildren(element);

    return Stream.concat(Stream.of(element), childeren);
  }

  /// Get all children nodes.
  public Stream<? extends ContractLibAstElement> getAllChildren(ContractLibAstElement element) {
    Stream<? extends ContractLibAstElement> childeren = getDirectChildren(element);

    return Stream.concat(getDirectChildren(element), childeren.flatMap(this::getAllChildren));
  }

  /// Get all direct children of the node.
  public Stream<? extends ContractLibAstElement> getDirectChildren(ContractLibAstElement element) {
    return element.perform(
        this::getDirectChildrenAst,
        this::getDirectChildrenCommand,
        this::getDirectChildrenSortTerm,
        this::getDirectChildrenTerm,
        this::getDirectChildrenInner);
  }

  // Helper functions

  private Stream<? extends ContractLibAstElement> getDirectChildrenAst(ContractLibAst ast) {
    return Stream.of(
        ast.abstractions(),
        ast.asserts(),
        ast.constants(),
        ast.datatypes(),
        ast.sorts(),
        ast.sortParameter(),
        ast.constants(),
        ast.functions())
        .flatMap(List::stream);
  }

  // - Commands

  private Stream<? extends ContractLibAstElement> getDirectChildrenCommand(Command command) {

    //TODO: Implement
    return command.perform(
        this::getDirectChildrenAbstraction, //decAbstraction,
        this::getDirectChildrenJoinedCommand, //decAbstractions,
        _x -> Stream.of(), //decConstant,
        this::getDirectChildrenDatatype, //decDatatype,
        this::getDirectChildrenJoinedCommand, //decDatatypes,
        _x -> Stream.of(), //decFunction,
        _x -> Stream.of(), //decParameter,
        this::getDirectChildrenSortDec, //decSort,
        this::getDirectChildrenSortDec, //defSort, // TODO:: Add children of SortTerm
        _x -> Stream.of(), //defContract,
        _x -> Stream.of(), //defFunction,
        _x -> Stream.of(), //defFunctionRec,
        this::getDirectChildrenJoinedCommand, //defFunctionsRec,
        _x -> Stream.of());//assertion);
  }

  private Stream<? extends ContractLibAstElement> getDirectChildrenJoinedCommand(
      JoinedCommand<? extends JoinableCommand<?>> joinedCommand) {
    return joinedCommand.commands().stream();
  }

  private Stream<? extends ContractLibAstElement> getDirectChildrenSortDec(SortDec sortDec) {
    return Stream.of(
        sortDec.name(),
        sortDec.rank());
  }

  private Stream<? extends ContractLibAstElement> getDirectChildrenAbstraction(Abstraction abstraction) {
    return Stream.of(
        abstraction.identifier(),
        abstraction.datatypeDec());
  }

  private Stream<? extends ContractLibAstElement> getDirectChildrenDatatype(Datatype datatype) {
    return Stream.of(
        datatype.identifier(),
        datatype.dtDec());
  }

  // - Sort Terms

  private Stream<? extends ContractLibAstElement> getDirectChildrenSortTerm(Sort sort) {
    return sort.perform(
        this::getDirectChildrenSort,
        this::getDirectChildrenParametricSort);
  }

  private Stream<? extends ContractLibAstElement> getDirectChildrenSort(Type sort) {
    return Stream.of(sort);
  }

  private Stream<? extends ContractLibAstElement> getDirectChildrenParametricSort(ParametricType parametricSort) {
    return Stream.concat(
        Stream.of(parametricSort),
        parametricSort.arguments().stream());
  }

  // - Terms

  private Stream<? extends ContractLibAstElement> getDirectChildrenTerm(Term term) {
    //TODO: Implement
    return term.perform(
        _x -> Stream.of(), //specConstant,
        _x -> Stream.of(), //identifierAs,
        _x -> Stream.of(), //identifierValue,
        _x -> Stream.of(), //methodApplication,
        _x -> Stream.of(), //old,
        _x -> Stream.of(), //booleanLiteral,
        _x -> Stream.of(), //numberLiteral,
        _x -> Stream.of(), //letBinding,
        _x -> Stream.of(), //quantorBinding,
        _x -> Stream.of(), //matchBinding,
        _x -> Stream.of());//attributes);
  }

  // - Helper

  private Stream<? extends ContractLibAstElement> getDirectChildrenInner(Inner inner) {
    //TODO: Implement
    return Stream.of();
  }
}
