(theory FiniteMap

:smt-lib-version 2.7
:written-by "Robert Brune"
:date "2025-09-30"
:last-updated "2025-09-30"

:note
"This definition of finite maps is used by contractlib
"

:sorts (
        (Map 2)
        (Set 1)
       )

:funs (
       (par (A B) (as map.emtpy (Map A B)))
       (par (A B) (map.singleton (A B) (Map A B)))
       (par (A B) (map.set ((Map A B) A B) (Map A B)))

       (par (A B) (map.get ((Map A B) A) B))
       (par (A B) (map.inDomain ((Map A B) A) Bool))

      ; (par (A B) (map.keys ((Map A B)) (Set A)))
      ; (par (A B) (map.values ((Map A B)) (Set A)))
      ; (par (A B) (map.union ((Map A B) (Map A B)) (Map A B)))
       )

:note
"Stight forward definition of a map, the keys are all value types, while the value can be of any element
This is NOT an axomization.
"


; not sure if those are really necessary / usefull 
;(par (A B) (map.member ((Map A B) A B) Bool))
;(par (A B) (map.subset ((Map A B) (Map A B)) Bool))
;(par (A B) (map.disjunct ((Map A B) (Map A B)) Bool)) ; Can be derived from two subset operations
;(par (A B) (map.inter ((Map A B) (Map A B)) (Map A B)))
;(par (A B) (map.minus ((Map A B) (Map A B)) (Map A B)))
