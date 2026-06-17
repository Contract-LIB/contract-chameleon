# Integration Tests

## Overview

### Simple Tests

| Tests | Idea | VeriFast | KeY |
| ------------- | -------------- | -------------- | --- |
| example.Counter | Minimalistic example | ✅ | ⭕️ |
| example.Stack | Slightly more complicated example | ✅| ⭕️ |

### Encapsulation Tests

| Tests | Idea | VeriFast | KeY |
| ------------- | -------------- | -------------- | --- |
| [encapsulation-example.LinkedCellList](#linkedCellList) | List of externally managed references | ✅ | ⭕️ |
| [encapsulation-example.PeerToPeer](#peerToPeer) | Transfer of state between abstractions | ✅ | ⭕️ |
| [encapsulation-example.Component](#component) | Limitation of ownership transfer | ❎ | ⭕️ |
| encapsulation-example.IntTreeSet | | ⭕️ | ⭕️ |
| encapsulation-example.TreeNode | | ⭕️ | ⭕️ |

### Theory Tests

| Tests | Idea | VeriFast | KeY |
| ------------- | -------------- | -------------- | --- |
| theories.Core |   | ⭕️ | ⭕️ |
| theories.Ref |   | ⭕️ | ⭕️ |
| theories.Seq |   | ⭕️ | ⭕️ |
| theories.FiniteMap |   | ⭕️ | ⭕️ |
| theories.FiniteSet |   | ⭕️ | ⭕️ |

### Feature Tests

| Tests | Idea | VeriFast | KeY |
| ------------- | -------------- | -------------- | --- |
| features.Let | Support for C-LIB let bindings | ⭕️ | ⭕️ |
| features.Match | Support for C-LIB match expressions  | ⭕️ | ⭕️ |
| features.Quantifiers | Support for C-LIB quantifiers | ⭕️ | ⭕️ |
| features.Datatypes | Support for C-LIB data type definitions | ⭕️ | ⭕️ |

### Case Studies

| Tests | Idea | VeriFast | KeY |
| ------------- | -------------- | -------------- | --- |
| Kärkkäinen-Sanders algorithm | Creates a suffix array | ⭕️ | ⭕️ |
|  - The algorithm itself |  | ⭕️ | ⭕️ |
|  - The RB-Tree |  | ⭕️ | ⭕️ |

- ✅: Test successful
- ❎: Exploration of theoretical limitation (Test disabled)
- ⭕️: Test not defined
- ❌: Test failing

## Test Descriptions

### encapsulation-example.LinkedCellList {#linkedCellList}

References that get stored in a list and can be accessed later.
The state of the references always has to be managed externally.

### encapsulation-example.PeerToPeer {#peerToPeer}

Minimal example of one peer copying the state of a second peer.
Defined as contract.

### ❎ encapsulation-example.Component {#component}

The generated client and implementation classes hold notes about the limitation,
and the problems that arise at the moment.

A component persists of list of more components (recursively).

## Ideas for Case Studies

| Tests | Idea | VeriFast | KeY |
| ------------- | -------------- | -------------- | --- |
| Kärkkäinen-Sanders algorithm | Creates a suffix array | ⭕️ | ⭕️ |
|  - The algorithm itself |  | ⭕️ | ⭕️ |
|  - The RB-Tree |  | ⭕️ | ⭕️ |

### Created with [ChatGPT-5.5](https://ki-toolbox.scc.kit.edu/c/e3eaf169-67c4-44f5-bcc9-c1248e18e1eb)

#### Especially “map/set-centered” algorithms

- Dijkstra with ordered set
- A* search
- Sweep-line segment intersection
- Closest pair of points
- Skyline problem
- Calendar booking / interval overlap
- Sliding window median
- MEX maintenance
- Trie / suffix automaton using transition maps
- Inverted index search
- Word ladder BFS
- State-space search with visited set
- Memoized dynamic programming using state maps
- Disjoint interval management
- Sparse knapsack DP with maps

#### Graph Algorithms

- BFS / DFS graph traversal, visited is a set; adjacency list often map<Node, vector<Node>>
- Connected components, visited: set<Node> and component_id: map<Node, int>
- Cycle detection in graphs, visited and recursion_stack as sets
- Topological sort, indegree: map<Node, int>; zero_indegree: set<Node> or queue
- Dijkstra’s algorithm, dist: map<Node, number>; priority queue can be implemented with an ordered set<pair<dist,node>>
- A* search, open_set often a priority set; g_score, f_score, came_from are maps
- Uniform-cost search, Similar to Dijkstra: ordered set by current path cost
- Prim’s MST algorithm, Ordered set / priority queue of candidate edges; key: map<Node, weight>
- Kruskal’s MST algorithm, Edges sorted in a set; DSU may use maps for arbitrary node labels
- Tarjan SCC / articulation points,disc: map<Node,int>, low: map<Node,int>, on_stack: set<Node>
- Graph coloring color: map<Node, Color>; available colors stored in a set
- Bipartite checking, color: map<Node, 0/1> and visited set
- Shortest path reconstruction, parent/came_from: map<Node, Node>
- Transitive closure with memoization, reachable: map<Node, set<Node>>

#### Search Algorithms

- Binary search over answer + feasibility set, A set stores active candidates or constraints
- Bidirectional BFS,Two frontier sets: front_from_start, front_from_goal
- Maze/pathfinding search, visited: set<Cell>; parent: map<Cell, Cell>
- A*pathfinding, open_set, closed_set, came_from, g_score, f_score
- IDA* / backtracking search, visited set prevents revisiting states
- State-space search,seen: set<State> is often the key structure
- Puzzle solvers, e.g. 8-puzzle, dist: map<State,int>, parent: map<State,State>
- Word ladder, dictionary: set<string>, visited: set<string>
- Autocomplete search, map<char, TrieNode*> at each trie node
- Substring / dictionary matching, Trie transitions stored as maps
- Aho-Corasick string matching, next: map<char,state> and failure-link maps
- Inverted-index search, map<term, set<document_id>>

#### Greedy / scheduling algorithms

- - Interval scheduling
- Ordered set of intervals by end time
- Meeting room allocation
- multiset of current ending times
- Sweep-line algorithms
- Ordered set of active events/segments
- Calendar booking
- map<start,end> to find overlaps using predecessor/successor
- Job scheduling with deadlines
- Set of free time slots
- Task scheduler / event simulation
- map<time, events> or ordered set of events
- Least recently used cache
- map<key, iterator> plus ordered/list structure
- LFU cache
- map<frequency, set<keys>>

#### Computational Geometry

- Line segment intersection
- Sweep line with ordered set of active segments
- Closest pair of points
- Sliding window stored in ordered set by y-coordinate
- Rectangle union area
- Sweep-line events plus multiset of active y-intervals
- Skyline problem
- Multiset of active building heights
- Point location
- Ordered map/set for intervals or sweep status
- Convex hull variants
- Set/map can maintain active hull dynamically

#### Dynamic Programming / Memoization

- Memoized recursion
- map<State, Answer>
- Digit DP
- map<tuple<pos,tight,sum>, value>
- Tree DP with arbitrary labels
- map<Node, DPValue>
- Graph DP
- map<State, cost>
- Subset DP with sparse states
- map<bitmask, value>
- Knapsack with sparse weights
- map<weight, best_value>
- Longest increasing subsequence variant
- Ordered map from value to best length
- Edit distance with pruning
- Map/set of active states

#### String/search/data-indexing algorithms

- Trie construction
- map<char, Node*> children
- Aho-Corasick automaton
- Transition maps and output sets
- Suffix automaton
- Each state stores transitions in a map
- Inverted index
- map<string, set<int>> from word to documents
- N-gram indexing
- map<ngram, set<position>>
- Plagiarism detection
- Set of hashes/shingles
- Duplicate substring detection
- Set of substring hashes
- Anagram grouping
- Map from canonical sorted string to list of words
- Frequency counting
- map<Item, int>
- First non-repeating character
- Map of counts plus ordered set/queue of candidates

#### Very common competitive-programming patterns

- Coordinate compression
- map<value, compressed_id>
- Counting distinct elements in a window
- map/value count or set
- Sliding window median
- Two multisets
- Sliding window maximum/minimum
- Multiset or ordered map
- Offline queries
- Map/set stores active intervals or values
- Range queries with updates
- Ordered set of changed positions
- MEX maintenance
- Set of currently missing numbers
- Disjoint intervals
- map<left,right>
- Event compression
- map<coordinate, delta>
- Prefix-sum subarray counting
- map<prefix_sum, frequency>
