# Contract-LIB Tools

This module defines [generators](#generators)
for the creation of `Contract-LIB` `AST`
and [tooling](#available-tooling) working on the generated `Contract-LIB AST`.
Additionally, this module defines [labels](#ast-labels) for the generated `AST` nodes,
providing additional information about the specific node.

## Available Contexts (Tooling)

- `JavaSignatureContext`
  - `ClassNameExtractor`:
    Extract the identifier of `Java` class (and package identifier)
    from a `Contract-LIB` abstraction.
  - `JavaMethodSignatureExtractor`:
    Extract the `Java` signature of a method from a `Contract-LIB` contract.

- `ContractLibAstContext`
  - `ContractLibGenerator`:
    Generate a `Contract-LIB` from the `antlr4` parse tree.
  - Extensions
    - `FilePositionLinker` links the `AST` nodes to their position in the file.
    - `ParentLinker` access the parent `AST` node, if available.
    - `CommandOrderExtractor` represents the order where the commands where executed.
    - `DefFunctionIdentiferExtractor`
      collects all function identifiers defined by a `AST` node.
    - `DefSortIdentiferExtractor`
      collects all sort identifiers defined by a `AST` node.
    - `DefVariableIdentiferExtractor`
      collects all variable identifiers defined by a `AST` node.

- `AvailableIdentifierContext`:
  - `AvailableIdentifier<FunctionIdentifer>`:
    Access all available function identifiers (global).
  - `AvailableIdentifier<SortIdentifier>`:
    Access all available sort identifiers (global).
  - `AvailableIdentifier<VariableIdentifer>`:
    Access all available variable identifiers
    (for each scope in the `AST`).

- `ContractLibTypeContext`
  - _TODO:_ `ExpressionsTypeResolver`:
    Resolves the full type of each `Contract-LIB` expression.

## Checkers

- _TODO:_ `IdentifierDefinitionChecker`:
  Check if new defines identifier are unambiguous.
- _TODO:_ `IdentifierUsageChecker`:
  Check if accessed identifier are defined. (compare `TestVariableScopes`)

## `AST` Labels

The `Contract-LIB` `AST` nodes can be extended with labels.
This can happen directly in the translation from the parse tree
(`ContractLibAstTranslatorExtension`) or
though additional [tools](#available-tooling) working on the `AST`.

### Translator Extensions

- `FilePosition`:
  The position of the AST node
  where it is defined in the `Contract-LIB` source file.
- `ParentLinker`: Access the parent `AST` node, if available.
- `FunctionIdentiferExtractor`:
  Collect all function identifiers defined by a `AST` node.
- `SortIdentiferExtractor`
  Collect all sort identifiers defined by a `AST` node.
- `VariableIdentiferExtractor`
  Collect all variable identifiers defined by a `AST` node.

### Tool Extensions

- _TODO_: `TermTypeSignature`:
  The type signature of term expressions for the AST node.
- _TODO_: `AvailabeIdentifier<Sorts>`:
  Provides a list of sort identifiers that are available at this node.
- _TODO_: `AvailabeIdentifier<FunctionIdentifer>`:
  Provides a list of function identifiers that are available at this node.
  (TODO: differentiate between function and constants?)
