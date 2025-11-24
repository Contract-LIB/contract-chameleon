package example;

public abstract class Cell {

    //@ predicate pred(int absVal);

    public static Cell init(int id, int v)
    //@ requires true;
    //@ ensures result.pred(?result_absVal) &*& (result_absVal) == (v);
    {
        //TODO: Implement method 'example.Cell.init'.
        return null;
    }

    public abstract int value();
    //@ requires this.pred(?this_absVal_old) &*& true;
    //@ ensures this.pred(?this_absVal) &*& (result) == (this_absVal);
}
