(theory FiniteSet

:sorts ((Set 1))

:constants  

:funs ((par (A) (set.union ((Set A) (Set A)) (Set A)))
       (par (A) (set.inter ((Set A) (Set A)) (Set A)))
       (par (A) (set.minus ((Set A) (Set A)) (Set A)))

       (par (A) (set.member ((Set A) A) Bool))
       (par (A) (set.subset ((Set A) (Set A)) Bool))
       (par (A) (set.disjunct ((Set A) (Set A)) Bool)) ; Can be derived from two subset operations

       (par (A) (as set.emtpy (Set A)))
       (par (A) (set.singleton (A) (Set A)))

       (par (A) (set.card (Set A) Int))
       ;(par (A) (set.complement (Set A) (Set A)))
       ;(par (A) (as set.universe (Set A)))
      )



; Finite set definition from [cvc5](https://cvc5.github.io/docs/cvc5-1.3.0/theories/sets-and-relations.html)
"
Sort                   (Set <Sort>)
Constants              (declare-const X (Set Int))
Union                  (set.union X Y)
Intersection           (set.inter X Y)
Minus                  (set.minus X Y)
Membership             (set.member x X)
Subset                 (set.subset X Y)
Emptyset               (as set.empty (Set Int))
Singleton Set          (set.singleton 1)
Emptyset tester        (set.is_empty X)
Singleton tester       (set.is_singleton X)
Cardinality            (set.card X)
Insert / Finite Sets   (set.insert 1 2 3 (set.singleton 4))
Complement             (set.complement X)
Universe Set           (as set.universe (Set Int))

;Semantics

The semantics of most of the above operators (e.g., set.union, set.inter, difference) are straightforward. 
The semantics for the universe set and complement are more subtle and explained in the following.

The universe set (as set.universe (Set T)) is not interpreted as the set containing all elements of sort T.
Instead it may be interpreted as any set such that all sets of sort (Set T) are interpreted as subsets of it. In other words, it is the union of the interpretations of all (finite) sets in our input.

For example:

(declare-fun x () (Set Int))
(declare-fun y () (Set Int))
(declare-fun z () (Set Int))
(assert (set.member 0 x))
(assert (set.member 1 y))
(assert (= z (as set.universe (Set Int))))
(check-sat)
Here, a possible model is:

(define-fun x () (set.singleton 0))
(define-fun y () (set.singleton 1))
(define-fun z () (set.union (set.singleton 1) (set.singleton 0)))
Notice that the universe set in this example is interpreted the same as z, and is such that all sets in this example (x, y, and z) are subsets of it.

The set complement operator for (Set T) is interpreted relative to the interpretation of the universe set for (Set T),
and not relative to the set of all elements of sort T.
That is, for all sets X of sort (Set T),
the complement operator is such that (= (set.complement X) (set.minus (as set.universe (Set T)) X)) holds in all models.

The motivation for these semantics is to ensure that the universe set for sort T and applications of set complement can always be interpreted
as a finite set in (quantifier-free) inputs,
even if the cardinality of T is infinite.
Above, notice that we were able to find a model for the universe set of sort (Set Int) that contained two elements only.
"

)
