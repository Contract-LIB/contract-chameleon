package example;

public class LinkedCellListImpl extends LinkedCellList {

    //TODO: Implement 'example.LinkedCellList'.

    //@ predicate pred(list<Cell> absVal);

    LinkedCellListImpl()
    //@ requires true;
    //@ ensures this.pred(?result_absVal) &*& (this) != (null) &*& (result_absVal) == (nil);
    {
        //TODO: Implement 'example.LinkedCellList.init'.
    }

    public void add(Cell v)
    //@ requires this.pred(?this_absVal_old) &*& true;
    //@ ensures this.pred(?this_absVal) &*& (this_absVal) == (append(this_absVal_old, cons(v, nil)));
    {
        //TODO: Implement 'example.LinkedCellList.add'.
    }

    public Cell getLast()
    //@ requires this.pred(?this_absVal_old) &*& !((this_absVal_old) == (nil));
    //@ ensures this.pred(?this_absVal) &*& (result) != (null) &*& (result) == (nth((length(this_absVal)) - (1), this_absVal));
    {
        //TODO: Implement 'example.LinkedCellList.getLast'.
    }
}
