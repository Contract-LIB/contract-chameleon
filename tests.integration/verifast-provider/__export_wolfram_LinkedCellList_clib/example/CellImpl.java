package example;

public class CellImpl extends Cell {

    //@ predicate pred(int absVal);

    public int value()
    //@ requires this.pred(?this_absVal_old) &*& true;
    //@ ensures this.pred(?this_absVal) &*& (result) == (this_absVal);
    {
        //TODO: Implement method 'example.Cell.value'.
        return 0;
    }
}
