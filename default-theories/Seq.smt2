(theory Seq 

:smt-lib-version 2.7
:written-by "Robert Brune"
:date "2025-09-30"
:last-updated "2025-09-30"

:note
"This definition of sequences is used by contractlib,
it is oriented on the definitions from cvc5 and z3, see notes below.
"

:sorts ((Seq 1))

:funs (
        (par (A) (seq.empty (Seq A)))
        (par (A) (seq.unit (A) (Seq A)))
        (par (A) (seq.at ((Seq A) Int) (Seq A)))
        (par (A) (seq.extract ((Seq A) Int Int) (Seq A)))

        (par (A) (seq.len (Seq A) Int))
        (par (A) (seq.nth ((Seq A) Int) A))
        (par (A) (seq.indexof ((Seq A) A) Int))           ;only match one element

        (par (A) (seq.contains ((Seq A) (Seq A)) Bool))

        (par (A) (seq.++ ((Seq A) (Seq A)) (Seq A)))
        (par (A) (seq.update ((Seq A) Int Seq A) (Seq A)))
      )

      ;(par (A) (seq.prefixof ((Seq A) (Seq A)) Bool))
      ;(par (A) (seq.suffixof ((Seq A) (Seq A)) Bool))
      ;(par (A) (seq.indexof ((Seq A) (Seq A) Int) Int))
      ;(par (A) (seq.indexof ((Seq A) (Seq A)) Int))     ;without offset
      ;(par (A) (seq.replace ((Seq S) (Seq S) (Seq S)) (Seq S)))
      ;(par (A) (seq.replace_all ((Seq S) (Seq S) (Seq S)) (Seq S)))
      ;map
      ;mapi
      ;foldleft
      ;foldlefti


"
; Sequences definition from [cvc5](https://cvc5.github.io/docs/cvc5-1.3.0/theories/sequences.html)


(seq.empty (Seq S))                                       ⟦seq.empty⟧ = []

(seq.unit S (Seq S))                                      ⟦seq.unit⟧(x) = [x]

(seq.len (Seq S) Int)                                     ⟦seq.len⟧(s) is the length of the sequence s, denoted as |s|.

(seq.nth ((Seq S) Int) S)                                 ⟦seq.nth⟧(s, i) is the n-th element in the sequence s, denoted as nth(s, i).
                                                                          It is uninterpreted if i out of bounds,
                                                                          i.e. i < 0 or i >= |s|.

(seq.update ((Seq S) Int (Seq S)) (Seq S))                ⟦seq.update⟧(s, i, sub) is a sequence obtained by updating the continuous
                                                                                  sub-sequence of s starting at index i by sub.
                                                                                  The updated sequence has the same length as |s|.
                                                                                  If i + |sub| > |s|,
                                                                                  the out of bounds part of sub is ignored.
                                                                                  If i out of bounds, i.e. i < 0 or i >= |s|,
                                                                                  the updated sequence remains same with s.

(seq.extract ((Seq S) Int Int) (Seq S))                   ⟦seq.extract⟧(s, i, j) is the maximal sub-sequence of s that starts at
                                                                                 index i and has length at most j,
                                                                                 in case both i and j are non-negative and i is
                                                                                 smaller than |s|.
                                                                                 Otherwise, the return value is the empty sequence.

(seq.++ ((Seq S) (Seq S)) (Seq S))                        ⟦seq.++⟧(s1, s2) is a sequence that is the concatenation of s1 and s2.

(seq.at ((Seq S) Int) (Seq S))                            ⟦seq.at⟧(s, i) is a unit sequence that contains the i-th element of s as
                                                                         the only element, or is the empty sequence if i < 0 or i > |s|.

(seq.contains ((Seq S) (Seq S)) Bool)                     ⟦seq.contains⟧(s, sub) is true if sub is a continuous sub-sequence of s,
                                                                                 i.e. sub = seq.extract(s, i, j) for some i, j,
                                                                                 and false if otherwise.

(seq.indexof ((Seq S) (Seq S) Int) Int)                   ⟦seq.indexof⟧(s, sub, i) is the first position of sub at or after i in s,
                                                                                   and -1 if there is no occurrence.

(seq.replace ((Seq S) (Seq S) (Seq S)) (Seq S))           ⟦seq.replace⟧(s, src, dst) is the sequence obtained by replacing the
                                                                                     first occurrence of src by dst in s.
                                                                                     It is s if there is no occurrence.

(seq.replace_all ((Seq S) (Seq S) (Seq S)) (Seq S))       ⟦seq.replace_all⟧(s, src, dst) is the sequence obtained by replacing all
                                                                                         the occurrences of src by dst in s,
                                                                                         in the order from left to right.
                                                                                         It is s if there is no occurrence.

(seq.rev (Seq S) (Seq S))                                 ⟦seq.rev⟧(s) is the sequence obtained by reversing s.

(seq.prefixof ((Seq S) (Seq S)) Bool)                     ⟦seq.prefixof⟧(pre s) is true if pre is a prefix of s, false otherwise.

(seq.suffixof ((Seq S) (Seq S)) Bool)                     ⟦seq.suffixof⟧(suf s) is true if suf is a suffix of s, false otherwise.


; Sequence definition from [z3](https://microsoft.github.io/z3guide/docs/theories/Sequences)

(seq.unit elem) 	            Sequence with a single element elem
(as seq.empty (Seq Int))	    The empty sequence of integers
(seq.++ a b c)	              Concatenation of one or more sequences
(seq.len s)	                  Sequence length. Returns an integer
(seq.extract s offset length)	Retrieves sub-sequence of s at offset
(seq.indexof s sub [offset])	Retrieves first position of sub in s, -1 if there are no occurrences
(seq.at s offset)	            Sub-sequence of length 1 at offset in s
(seq.nth s offset)	          Element at offset in s. If offset is out of bounds the result is under-specified. In other words, it is treated as a fresh variable
(seq.contains s sub)	        Does s contain the sub-sequence sub?
(seq.prefixof pre s)	        Is pre a prefix of s?
(seq.suffixof suf s)	        Is suf a suffix of s?
(seq.replace s src dst)	      Replace the first occurrence of src by dst in s
(seq.map fn s)	              Map function (an expression of sort (Array S T)) on sequence s of sort (Seq S)
(seq.mapi fn o s)	            Map function (an expression of sort (Array Int S T)) on offset o and sequence s of sort (Seq S)
(seq.fold_left fn b s)	      Fold function (an expression of sort (Array T S T)) on initial value b of sort T and sequence s of sort (Seq S)
(seq.fold_lefti fn o b s)	    Fold function (an expression of sort (Array Int T S T)) on offset o, initial value b of sort T and sequence s of sort (Seq S)
"
)
