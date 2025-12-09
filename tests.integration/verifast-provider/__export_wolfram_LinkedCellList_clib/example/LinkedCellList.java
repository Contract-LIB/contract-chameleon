package example;

public abstract class LinkedCellList {

    //@ predicate pred(list<Cell> absVal);

    public static LinkedCellList init()
    //@ requires true;
    //@ ensures result.pred(?result_absVal) &*& (result_absVal) == (nil);
    {
        //TODO: Implement method 'example.LinkedCellList.init'.
        return null;
    }

    public abstract void add(Cell v);
    //@ requires this.pred(?this_absVal_old) &*& true;
    //@ ensures this.pred(?this_absVal) &*& (this_absVal) == (append(this_absVal_old, cons(v, nil)));

    public abstract Cell getLast();
    //@ requires this.pred(?this_absVal_old) &*& !((this_absVal_old) == (nil));
    //@ ensures this.pred(?this_absVal) &*& (result) == (nth((length(this_absVal)) - (1), this_absVal));
}
