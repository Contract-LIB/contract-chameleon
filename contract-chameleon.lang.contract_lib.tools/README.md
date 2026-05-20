# Contract-LIB Tools

This module defines [generators](#generators)
for the creation of `Contract-LIB` AST
and [tooling](#available-tooling) working on the generated `Contract-LIB` AST.
Additionally, this module defines [labels](#ast-labels) for the generated AST nodes,
providing additional information about the specific node.

## Generators

- `ContractLibGenerator`:
  Generate a `Contract-LIB` from the `antlr4` parse tree.

## Available Tooling

- `ClassNameExtractor`:
  Extract the identifier of `Java` class (and package identifier)
  from a `Contract-LIB` abstraction.
- `JavaMethodSignatureExtractor`:
  Extract the `Java` signature of a method from a `Contract-LIB` contract.
- _TODO:_ `ExpressionsTypeResolver`:
  Resolves the full type of each `Contract-LIB` expression.
- _TODO:_ `IdentifierDefinitionChecker`:
  Check if new defines identifier are unambiguous.
- _TODO:_ `IdentifierUsageChecker`:
  Check if accessed identifier are defined.

## AST Labels

The `Contract-LIB` AST nodes can be extended with labels.
This can happen directly in the translation from the parse tree
(`ContractLibAstTranslatorExtension`) or
though additional [tools](#available-tooling) working on the AST.

### Translator Extensions

- _TODO:_ `FilePosition`:
  The position of the AST node
  where it is defined in the `Contract-LIB` source file.
- _TODO:_ `AddedIdentifier<Global>`:
  The identifier added by this node to the global scope.
- _TODO:_ `AddedIdentifier<Local>`:
  The identifier added by this node to the local scope.

### Tool Extensions

- _TODO_: `ExpressionsTypeSignature`:
  The type signature of expressions for the AST node.
- _TODO_: `AvailabeIdentifier<Sorts>`:
  Provides a list of sort identifiers that are available at this node.
- _TODO_: `AvailabeIdentifier<FunctionIdentifer>`:
  Provides a list of function identifiers that are available at this node.
  (TODO: differentiate between function and constants?)
