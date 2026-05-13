# Integration Tests

## Overview

| Tests | Idea | VeriFast | KeY |
| ------------- | -------------- | -------------- | --- |
| example.Counter | | ✅ | ⭕️ |
| example.Stack | | ✅| ⭕️ |

| Tests | Idea | VeriFast | KeY |
| ------------- | -------------- | -------------- | --- |
| [encapsulation-example.LinkedCellList](#linkedCellList) | List of externally managed references | ✅ | ⭕️ |
| [encapsulation-example.PeerToPeer](#peerToPeer) | Transfer of state between abstractions | ✅ | ⭕️ |
| [encapsulation-example.Component](#component) | Limitation of ownership transfer | ❎ | ⭕️ |
| encapsulation-example.IntTreeSet | | ⭕️ | ⭕️ |
| encapsulation-example.TreeNode | | ⭕️ | ⭕️ |

| Tests | Idea | VeriFast | KeY |
| ------------- | -------------- | -------------- | --- |
| theories.Core |   | ⭕️ | ⭕️ |
| theories.Ref |   | ⭕️ | ⭕️ |
| theories.Seq |   | ⭕️ | ⭕️ |
| theories.FiniteMap |   | ⭕️ | ⭕️ |
| theories.FiniteSet |   | ⭕️ | ⭕️ |

| Tests | Idea | VeriFast | KeY |
| ------------- | -------------- | -------------- | --- |
| features.Let | Support for C-LIB let bindings | ⭕️ | ⭕️ |
| features.Match | Support for C-LIB match expressions  | ⭕️ | ⭕️ |
| features.Quantifiers | Support for C-LIB quantifiers | ⭕️ | ⭕️ |
| features.Datatypes | Support for C-LIB data type definitions | ⭕️ | ⭕️ |

- ✅: Test successful
- ❎: Exploration of theoretical limitation (Test disabled)
- ⭕️: Test not defined
- ❌: Test failing

## encapsulation-example.LinkedCellList (#linkedCellList)

References that get stored in a list and can be accessed later.
The state of the references always has to be managed externally.

## encapsulation-example.PeerToPeer (#peerToPeer)

Minimal example of one peer copying the state of a second peer.
Defined as contract.

## ❎ encapsulation-example.Component (#component)

The generated client and implementation classes hold notes about the limitation,
and the problems that arise at the moment.

A component persists of list of more components (recursively).
