package example;

public class LinkedCellListImpl extends LinkedCellList {

    //@ predicate pred(list<Cell> absVal);

    LinkedCellListImpl()
    //@ requires true;
    //@ ensures result.pred(?result_absVal) &*& (result) != (null) &*& (result_absVal) == (nil);
    {
        //TODO: Implement method 'example.LinkedCellList.init'.
    }

    public void add(Cell v)
    //@ requires this.pred(?this_absVal_old) &*& true;
    //@ ensures this.pred(?this_absVal) &*& (this_absVal) == (append(this_absVal_old, cons(v, nil)));
    {
        //TODO: Implement method 'example.LinkedCellList.add'.
    }

    public Cell getLast()
    //@ requires this.pred(?this_absVal_old) &*& !((this_absVal_old) == (nil));
    //@ ensures this.pred(?this_absVal) &*& (result) != (null) &*& (result) == (nth((length(this_absVal)) - (1), this_absVal));
    {
        //TODO: Implement method 'example.LinkedCellList.getLast'.
    }
}
