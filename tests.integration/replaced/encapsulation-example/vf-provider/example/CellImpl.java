package example;

public class CellImpl extends Cell {

    private int x;

    //@ predicate pred(int absVal) = x |-> absVal;

    CellImpl()
    //@ requires true;
    //@ ensures pred(?result_absVal) &*& (this) != (null) &*& (result_absVal) == (0);
    {
        this.x = 0;
    }

    public int value()
    //@ requires pred(?this_absVal_old) &*& true;
    //@ ensures pred(?this_absVal) &*& (result) == (this_absVal);
    {
        return this.x;
    }

    public void set(int v)
    //@ requires pred(?this_absVal_old) &*& true;
    //@ ensures pred(?this_absVal) &*& (this_absVal) == (v);
    {
        this.x = v;
    }
}
