
(theory Refs 

 :smt-lib-version 2.7
 :written-by "Robert Brune"
 :date "2025-09-30"
 :last-updated "2025-09-30"
 
 :sorts ((Ref 1))

 :funs (
        (par (A) (null (Ref A)))
        ;(= (Ref A) (Ref A) Bool)       ; equality, defined generic in Core 
        ;(distinct A A Bool :pairwise)) ;
 )

:values
"
- null, as the empty reference
- References to objects location
"

:funs-description
"The object identity of two referecenes."
)
