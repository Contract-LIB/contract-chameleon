package example;

public class LinkedCellListImpl extends LinkedCellList {

    //@ predicate pred(list<Cell> absVal);

    public void addLast(Cell v)
    //@ requires this.pred(?this_absVal_old) &*& true;
    //@ ensures this.pred(?this_absVal) &*& (this_absVal) == (append(this_absVal_old, cons(v, nil)));
    {
        //TODO: Implement method 'example.LinkedCellList.addLast'.
    }

    public Cell getLast()
    //@ requires this.pred(?this_absVal_old) &*& !((this_absVal_old) == (nil));
    //@ ensures this.pred(?this_absVal) &*& (result) == (nth((length(this_absVal)) - (1), this_absVal));
    {
        //TODO: Implement method 'example.LinkedCellList.getLast'.
        return null;
    }
}
