package example;

public abstract class Cell {

    //@ public instance ghost \locset footprint;


    //@ public instance invariant \subset(this.*, this.footprint);


    //@ public instance accessible \inv : footprint;


    //@ public instance ghost int absVal;


    /*@  normal_behavior
        requires (false | true);
        ensures (true & (true ==> \result.absVal == v));
        ensures \fresh(\result.footprint);
        */
    public static Cell init(int id, int v) {
        return new CellImpl(id, v);
    }

    /*@  normal_behavior
        requires (false | true);
        ensures (true & (true ==> \result == this.absVal));
        accessible this.footprint;
        assignable this.footprint;
        */
    public abstract int value();
}
