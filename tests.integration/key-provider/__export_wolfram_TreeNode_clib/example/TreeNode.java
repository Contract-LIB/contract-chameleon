package example;

public abstract class TreeNode {

    //@ public instance ghost \locset footprint;


    //@ public instance invariant \subset(this.*, this.footprint);


    //@ public instance accessible \inv : footprint;


    //@ public instance ghost \dl_Set absVal;


    /*@  normal_behavior
        requires (false | true);
        ensures (true & (true ==> \result.absVal == \dl_sSingleton(v)));
        ensures \fresh(\result.footprint);
        ensures \invariant_for(\result);
        */
    public static TreeNode init(int v) {
        return new TreeNodeImpl(v);
    }

    /*@  normal_behavior
        requires (false | true);
        ensures (true & (true ==> (\forall int x;\dl_sElementOf(\old(this.absVal), x) || x == v) && \dl_sElementOf(this.absVal, v)));
        accessible this.footprint;
        assignable this.footprint;
        */
    public abstract void add(int v);

    /*@  normal_behavior
        requires (false | true);
        ensures (true & (true ==> \result == \dl_sElementOf(this.absVal, v)));
        accessible this.footprint;
        assignable this.footprint;
        */
    public abstract boolean contains(int v);
}
