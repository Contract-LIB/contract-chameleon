package example;

public abstract class TreeNode {

    //@ public instance ghost \locset footprint;


    //@ public instance invariant \subset(this.*, this.footprint);


    //@ public instance accessible \inv : footprint;


    //@ public instance ghost \dl_set absVal;


    /*@  normal_behavior
        requires (false | true);
        ensures (true & (true ==> this.absVal == \dl_finiteSetSingleton(v)));
        ensures \fresh(\result.footprint);
        accessible this.footprint;
        assignable this.footprint;
        */
    public static void init(int v) {
        //NOTE: This should be never called, as it is only the interface!
        return null;
    }

    /*@  normal_behavior
        requires (false | true);
        ensures (true & (true ==> (\forall int x;\dl_finiteSetMember(x, \old(this.absVal)) || x == v) && \dl_finiteSetMember(v, this.absVal)));
        accessible this.footprint;
        assignable this.footprint;
        */
    public abstract void add(int v);

    /*@  normal_behavior
        requires (false | true);
        ensures (true & (true ==> \result == \dl_finiteSetMember(v, this.absVal)));
        accessible this.footprint;
        assignable this.footprint;
        */
    public abstract boolean contains(int v);
}
