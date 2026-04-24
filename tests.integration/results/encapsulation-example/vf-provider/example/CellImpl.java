package example;

public class CellImpl extends Cell {

    //TODO: Implement 'example.Cell'.

    //@ predicate pred(int absVal);

    CellImpl()
    //@ requires true;
    //@ ensures this.pred(?result_absVal) &*& (this) != (null) &*& (result_absVal) == (0);
    {
        //TODO: Implement 'example.Cell.init'.
    }

    public int value()
    //@ requires this.pred(?this_absVal_old) &*& true;
    //@ ensures this.pred(?this_absVal) &*& (result) == (this_absVal);
    {
        //TODO: Implement 'example.Cell.value'.
    }

    public void set(int v)
    //@ requires this.pred(?this_absVal_old) &*& true;
    //@ ensures this.pred(?this_absVal) &*& (this_absVal) == (v);
    {
        //TODO: Implement 'example.Cell.set'.
    }
}
