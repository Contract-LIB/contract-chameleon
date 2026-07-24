# Alias Problem

The following example look at some special cases of the alias problem,
the semantics in `ContractLIB` and
how they can (or cannot) be translated to the tools.

Abstractions can appear in three modes in a contract,
which bring the following conditions along.

## Base Examples

This is our first base abstractions,
working as a `Int` wrapper.

```lisp
(declare-abstractions
  (
    (IntAbstraction 0)
  )
  (
    ((IntAbstraction (absVal Int)))
  )
)
; Constructor
(define-contract alias.IntAbstraction.init
    (
      (result (out IntAbstraction))
    )
    ((
      true
      (= (absVal result) 0)
    ))
)

; Getter
(define-contract alias.IntAbstraction.get
    (
      (this (in IntAbstraction))
      (result (out Int))
    )
    ((
      true
      (= (absVal this) result)
    ))
)

; Setter 
(define-contract alias.IntAbstraction.set
    (
      (this (inout IntAbstraction))
      (value (in Int))
    )
    ((
      true
      (= (absVal this) value)
    ))
)
```

Our second base abstraction is a wrapper for a reference,
or an alias.
We require that the inner type of the reference
is also an abstraction.

One might notice that the getter and setter contracts
are similar between the `RefAbstraction` and the `IntAbstraction`.
This results from the idea,
that a `Ref T` is rather a value type
(possibly `null`, or the unique object it points to)
then a transparent reference.
This however, comes at the cost,
that we cannot access the fields or state from the abstraction `T`
from a `Ref T` variable or argument.

```lisp
(declare-abstractions
  (
    (RefAbstraction 0)
  )
  (
    ((RefAbstraction (absVal (Ref IntAbstraction))))
  )
)
; Constructor
(define-contract alias.RefAbstraction.init
    (
      (result (out IntAbstraction))
    )
    ((
      true
      (= (absVal result) null)
    ))
)

; Getter
(define-contract alias.RefAbstraction.get
    (
      (this (in RefAbstraction))
      (result (out (Ref IntAbstraction)))
    )
    ((
      true
      (= (absVal this) result)
    ))
)

; Setter
(define-contract alias.RefAbstraction.set
    (
      (this (inout RefAbstraction))
      (value (in (Ref IntAbstraction)))
    )
    ((
      true
      (= (absVal this) value)
    ))
)
```

## Two Abstraction Contracts

We are able to define contracts,
that change their state and the one of the second given abstraction.
We also take a look at the different modes,
under which the abstractions might appear
and their consequences seen from a client perspective.

```lisp
(define-contract alias.IntAbstraction.copyStateFrom
    (
      (this (inout IntAbstraction))
      (from (in IntAbstraction))
    )
    ((
      true
      (= (absVal this) (absVal this))
    ))
)
```

```lisp
(define-contract alias.IntAbstraction.copyStateTo
    (
      (this (in IntAbstraction))
      (from (inout IntAbstraction))
    )
    ((
      true
      (= (absVal this) (absVal this))
    ))
)
```

Let's also look at some erroneous definitions,
that look valid on the first glimpse,
but miss what we wanted to express.

```lisp
(define-contract alias.IntAbstraction.copyStateToWrongA
    (
      (this (inout IntAbstraction))
      (from (inout IntAbstraction))
    )
    ((
      true
      ; We ensure that both abstractions have the same state.
      ; However, this state is not connected to any of the 'old' states
      ; of the abstractions under which the me contract was called.
      ; Asigning an aritrary value to both abstractions,
      ; would be a correct implementation of this contract.
      (= (absVal this) (absVal this))
    ))
)
```

## Losing Information

As we declare abstractions,
and we say that `T` in `Ref T` only must be an abstraction,
one might come to the idea to let an abstraction own a reference of the self type.
Or create contracts where an abstraction is passed a reference to itself.
As this breaks the encapsulation requirement of the arguments on first sight,
I will argue with the following example,
that this is not the case.

```lisp
(declare-abstractions
  (
    (alias.LoosingAbstraction 0)
  )
  (
    ((LoosingAbstraction 
      (absValRef (Ref LoosingAbstraction))
      (absVal Int)))
  )
)
; Getter Ref
(define-contract alias.LoosingAbstraction.getRef
    (
      (this (in LoosingAbstraction))
      (result (out (Ref LoosingAbstraction)))
    )
    ((
      true
      (and
        (= (absValRef result) null)
        (= (absVal result) 0)
      )
    ))
)
; Getter Value
(define-contract alias.SpecialAbstraction.getValue
    (
      (this (in SpecialAbstraction))
      (result (out Int))
    )
    ((
      true
      (= (absVal this) result)
    ))
)
; Setter Ref
(define-contract alias.LoosingAbstraction.setRef
    (
      (this (inout LoosingAbstraction))
      (value (in (Ref LoosingAbstraction)))
    )
    ((
      true
      (and
        (= (absValRef this) value)
        (= (absVal this) (old (absVal  this)))
      )
    ))
)
; Setter Value
(define-contract alias.SpecialAbstraction.setValue
    (
      (this (inout SpecialAbstraction))
      (value (in Int))
    )
    ((
      true
      (and
        (= (absVal this) value)
        (= (absValRef this) (old (absValRef this)))
      )
    ))
)
```

This contract looses information about the state of the abstraction,
as a consequence we are not able to work with the reference returned afterward.

```lisp
(define-contract alias.LoosingAbstraction.looseInfo
    (
      (this (inout SpecialAbstraction))
    )
    ((
      true
      true
    ))
)
```

This is the `KeY` implementation,
where we can see how this limits our ability to proof the client.

```java
/*@  normal_behavior
    requires true;
    ensures true;
    ensures \new_elems_fesh(footprint);
    assignable this.footprint;
    */
public abstract void looseInfo() {
}
```

```java
/*@ normal_behavior
  requires true;
  ensures true;
*/
void client() {
    SpecialAbstraction a = SpecialAbstraction.init();
    a.setRef(a);

    SpecialAbstraction c = a.getRef();
    //@ assert c == a;

    //loses information about the reference it holds, and its value
    a.looseInfo();

    SpecialAbstraction d = a.getRef();
    // After loosing the information about the reference,
    // the following assertion does not hold anymore.
    // We can also not sure what state d is in.
    // @ assert d == a;
    // @ assert d != null;

    // Lets ensure that d is not `null`.
    if (d == null) {
      d = SpecialAbstraction.init();
    }

    //@ assert d != null;

    // We are not able know where d resighns on the heap,
    // So verification failes here,
    // even though we know that d.setValue(10) is a valid call.
    //d.setValue(10);
}
```

## The Alias Problem in `KeY`

An issue arises,
when we use an abstraction as an internal owned field,
and provide a contract where we can link this owned field,
to another outside abstraction,
as well as provide a direct setter to this owned field.

However, this comes down to a super specific implementation detail,
where we intensionally break the well encapsulation property.
This also only affects the provider,
where we provide a malicious implementation,
which intensionally breaks the access restriction of the `Ref` type.
This access restriction cannot be enforced by `KeY` at the moment.
Through this example,

I want to show that we need some kind of restrictions on the footprint,
to ensure, that the references cannot trigger unexpected side-effects.

See the following example for more details:

### Alias Problem Example

```lisp
(declare-abstractions
  (
    (alias.BadAliasAbstraction 0)
  )
  (
    ((BadAliasAbstraction
      (absState Int)
      (absValue Int)))
  )
)
; Constructor
(define-contract alias.BadAliasAbstraction.init
    (
      (result (out BadAliasAbstraction))
    )
    ((
      true
      (and
        (= (absValue result) 0)
        (= (absState result) 0)
      )
    ))
)

; Getter
(define-contract alias.BadAliasAbstraction.getValue
    (
      (this (in BadAliasAbstraction))
      (result (out Int))
    )
    ((
      true
      (= (absValue this) result)
    ))
)
(define-contract alias.BadAliasAbstraction.getState
    (
      (this (in BadAliasAbstraction))
      (result (out Int))
    )
    ((
      true
      (= (absValue this) result)
    ))
)

; Setter
(define-contract alias.BadAliasAbstraction.setState
    (
      (this (inout BadAliasAbstraction))
      (state (in Int))
    )
    ((
      true
      (and
        (= (absState this) state)
        (= (absValue this) (old (absValue this)))
      )
    ))
)
(define-contract alias.BadAliasAbstraction.setValue
    (
      (this (inout BadAliasAbstraction))
      (value (in Int))
    )
    ((
      true
      (and
        (= (absState this) (old (absState this)))
        (= (absValue this) value)
      )
    ))
)

(define-contract alias.BadAliasAbstraction.setBoth
    (
      (this (inout BadAliasAbstraction))
      (value (in Int))
    )
    ((
      true
      (and
        (= (absState this) value)
        (= (absValue this) value)
      )
    ))
)

; Bad Alias Contract
(define-contract alias.BadAliasAbstraction.invalid
    (
      (this (inout BadAliasAbstraction))
      (value (in (Ref BadAliasAbstraction)))
    )
    ((
      (= (absValue this) 10)
      (and 
        (= (absState this) 10)
        (= (absValue this) 10)
      )
    ))
)
```

### The Malicious `KeY` Implementation

If we translate this straight forward to a `JML` contract,
we could run into error,
but as this is not a valid `ContractLIB` specification,
our `Ref` type protects us from these invalid accesses.

```java

int value;
BadAliasAbstraction ref;

//@ public invariant this.value == this.absValue;
//@ public invariant this.absState == (ref == null ? 0 : ref.absValue);

// From super class
//@ public invariant \subset(this.*, this.fp);

/*@  normal_behavior
    requires true;
    requires value.absValue = 10;
    ensures this.absState = 10;
    ensures value.absValue = 10;
    ensures \new_elems_fesh(footprint);
    assignable this.footprint;
    */
public abstract void invalid(BadAliasAbstraction value) {
  //This would not be valid in VF, as we cannot return the predicate for value afterwards.
  this.ref = value;

  /* And example for a correct implementation:
  this.ref = BadAliasAbstraction.init();
  this.ref.setValue(10);
  */

  // We need to update our footprint
  //@ set this.fp = \all_fields(this) + ref.fp;
  // And updtate our absState
  //@ set this.absState = 10;
}

/*@  normal_behavior
    requires true;
    ensures this.absState = value;
    ensures this.absValue = \old(this.absValue);
    ensures \new_elems_fesh(footprint);
    assignable this.footprint;
    */
public abstract void setState(int value) {
  if (this.ref == null) {
    this.ref = BadAliasAbstraction.init();
    this.ref.setValue(value);
  } else {
    this.ref.setValue(value);
  }
  //@ set this.absState = value;
}
```

Problem in the implementation:
To verify the setter contracts,
we have to add the following requirement,
which is not guaranteed by the contract, nor by the invalid method.
So it becomes super difficult to actually create and verify a broken implementation.

```
requires this.ref != this; //TODO: Test this requirement
```

```java
// The aliasing problem
/*@ normal_behavior
  requires true;
  ensures true;
*/
void invalidClient() {
    BadAliasAbstraction a = BadAliasAbstraction.init();
    a.setValue(10);

    BadAliasAbstraction b = BadAliasAbstraction.init();
    b.setValue(10);

    a.invalid(b);

    // ERROR: This would trigger a sideeffect on b
    a.setBoth(10);

    //@ assert a.absState == 10; 
    //@ assert a.absValue == 10; 
    //@ assert b.absValue == 10; // this is not intended!
}
```

```java
// The self aliasing problem
/*@ normal_behavior
  requires true;
  ensures true;
*/
void invalidClientSelf() {
    BadAliasAbstraction a = BadAliasAbstraction.init();
    // a.absState -> a.absValue;
    a.invalid(a);

    // ERROR: This would trigger a sideeffect on self
    a.setState(10);
    //@ assert a.absState == 10; 
    //@ assert a.absValue == 10; // this is not intended!
}
```

This example shows
why we would need the `\disjoint(fp, value.pf)` clause in the reference contracts.

### Solutions to the Alias Problem

#### Stronger Encapsulation Requirement for References, Disallow Self-Reference

- This limits the contracts on side of `KeY`
- We are not able to pass `this` to the class itself any more
- Questions: Can we have a stronger statement also in the ensures clause?

```java
/*@  normal_behavior
    requires true;
    requires value.absVal = 10;
    requires \disjoint(this.fp, \reachLocs(value));
    ensures this.absValState = 10;
    ensures \disjoint(this.fp, \reachLocs(value));
    ensures \new_elems_fesh(footprint);
    assignable this.footprint;
    */
public abstract void invalid(Counter value) {
  //This would not be valid in VF, as we cannot return the predicate for value afterwards.
  this.ref = value;
  
  /* The wanted implementation:
  this.ref = new BadSpecialAbstraction();
  this.ref.setValue(10);
  */

  // We need to update our footprint, and abstract state
  //@ set this.fp = \all_fields(this);
  //@ set this.absValState = 10;
}
```

#### This == value || \disjoint(fp, value.fp)

#### Idea: State Independence

Disallow the state of fields of references affect the abstract state of the abstraction

- Questions:
  - Does this also hold with multiple references in chain?
  - What about cross-references, multiple `inout` abstractions and their references?
- Limitation:
  - Additional restrictions between multiple states in the total state space
      through invariants (e.g., `this.a + this.b > 300`)
      as those states would need to be changed at once

```java
/*@  normal_behavior
    requires true;
    requires value.absVal = 10;
    ensures this.absValState = 10;
    ensures value.absVal = 10;
    ensures forall int x: [value.absValue = x](this.absState == old(this.absState)) // (I)
    ensures forall int x: [value.absState = x](this.absValue == old(this.absValue)) // (II)
    ensures \new_elems_fesh(footprint);
    assignable this.footprint;
    */
public abstract void invalid(BadSpecialAbstraction value) {

  this.ref = value;

  //This would not be valid in VF, as we cannot return the predicate for value afterwards.

  // The condition (I) would not hold anymore
  // as changing the state of absVal would also affect the state of absState.
  // Generally, this consists of state change for one field,
  // and the the unchangednes for all other fields.

  /* The wanted implementation:
  this.ref = new BadSpecialAbstraction();
  this.ref.setValue(10);
  */

  // We need to update our footprint
  //@ set this.fp = \all_fields(this);
  //@ set this.absState = 10;
}
```

#### Opaque References

- Annotation like the generic wrapper in `ContractLIB` that holds the reference
- Can only be stored to variables that also own the `/*@ opaque @*/` annotation
- Connection Universe Types: `/*@ opaque @*/` == `@Payload`

```java
/*@ opaque @*/ BadSpecialAbstraction ref;

BadSpecialAbstraction ownedRef;

/*@  normal_behavior
    requires true;
    requires value.absVal = 10;
    ensures this.absValState = 10;
    ensures \new_elems_fesh(footprint);
    assignable this.footprint;
    */
public abstract void invalid(/*@ opaque @*/BadSpecialAbstraction value) {
  this.ref = value;
  this.ownedRef = value; // This is disallowed

  int x = this.ref.getValue(); // This is disallowed as ref is opaque
  // This would also be disallowed in an invariant,…
}
```

## Limitation of `KeY` Payload

This example shows,
that we are able to verify `assert false;`
with the current translation of the `Ref` type
in a multi-tool setup.

```java
// Verified with VF
class Client {
    static void client() {
        C c = new C();
        
        c.proc(c);
    }
}

// Verified with KeY
class C {
    //@ invariant fp not empty

    // @ requires \disjoint(fp, p.fp); // ←− \reachLocs(p)
    //@ ensures \disjoint(fp, p.fp); // ←− \reachLocs(p)
    //@ ensures \new_elems_fresh(fp) && φ3 (absVal, \old(absVal));
    //@ assignable fp;
    void proc(C p) {
        if (p == this) {
            assert false;
        } else {
            ...
        }
    
    }
}
```

### Solutions

#### `SemiRef`

Enforces on all instances that the following predicate holds `ref != this`,
additionally to the footprint restrictions.
This is not a general payload type,
but one that we can express with `KeY`'s dynamic frames.

By adding this predicate to all terms,
the translation becomes semantics-maintaining,
between the different tools.

However, this type has this special restriction,
we would not expect from a `Payload` type.

#### Universe Type System

Using a `Universe Type System`'s `Payload` attribute,
we can express the payload properties in `KeY`.

## Conclusion

- dynamic frames cannot handle arbitrary payloads (limitation regarding `this`)
- dynamic frames with universe types can handle this problem

----

## Notes

```java
    //(this (inout StoreOwned))
    //(input (in Counter))

    /*@  normal_behavior
        requires \disjoint(this.footprint, input.footprint);
        ensures (this.absVal) == (\seq_concat(\old(this.absVal), \seq_singleton(input)))));
        ensures \disjoint(this.footprint, input.footprint);
        ensures \new_elems_fesh(footprint);
        accessible this.footprint;
        accessible input.footprint;
        assignable this.footprint;
        */
    public abstract int addOwned(Counter input) {}


    //TODO: Check for VF
    //(this (inout StoreOwned))
    //(input (in (Ref Counter)))

    /*@  normal_behavior
        requires true;
        ensures true;
        ensures \new_elems_fesh(footprint);
        accessible this.footprint;
        assignable this.footprint;
        */
    public abstract int addRef(Counter input) {
      this.box = input.gotBox()
    }

    //(this (inout StoreOwned))
    //(inputA (in (Ref Counter)))
    //(inputB (in (Ref Counter)))

    /*@  normal_behavior
        requires true;
        --requires \disjointX(this.fp, inputA.fp, intputB.fp);
        ensures true;
        --ensures \disjointX(this.fp, inputA.fp, intputB.fp);
        ensures \new_elems_fesh(footprint);
        accessible this.footprint;
        assignable this.footprint;
        */
    public abstract int addTwoRef(Counter inputA, Counter inputB) {}


    //s1 = new Counter();
    //s2 = new Counter();
    //s1.addTwoRef(s1, s2);

    //setValue(int x) { }

    //addRef(input) 
    //setValue(x)

    //a = Box [5]
    //b = a
    //b.cntent = 6
    //a.content = 6

    //    requires \disjoint(footprint, input.footprint)
    //    --ensures \fresh(footprint - old(footprint));
    //    ensures \disjoint(footprint, input.footprint)
```
