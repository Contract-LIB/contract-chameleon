package example;

public class CellImpl extends Cell {

    //@ predicate pred(int absVal);

    CellImpl()
    //@ requires true;
    //@ ensures result.pred(?result_absVal) &*& (result) != (null) &*& (result_absVal) == (0);
    {
        //Test
    }

    public int value()
    //@ requires this.pred(?this_absVal_old) &*& true;
    //@ ensures this.pred(?this_absVal) &*& (result) == (this_absVal);
    {
        //Test
    }

    public void set(int v)
    //@ requires this.pred(?this_absVal_old) &*& true;
    //@ ensures this.pred(?this_absVal) &*& (this_absVal) == (v);
    {
        //Test
    }
}
