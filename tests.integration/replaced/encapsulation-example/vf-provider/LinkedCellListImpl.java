package example;

public class LinkedCellListImpl extends LinkedCellList {

    //@ predicate pred(list<Cell> absVal);

    LinkedCellListImpl()
    //@ requires true;
    //@ ensures result.pred(?result_absVal) &*& (result) != (null) &*& (result_absVal) == (nil);
    {
        head = null;
    }

    public void add(Cell v)
    //@ requires this.pred(?this_absVal_old) &*& true;
    //@ ensures this.pred(?this_absVal) &*& (this_absVal) == (append(this_absVal_old, cons(v, nil)));
    {
        if (head == null) {
          head = new NonEmptyLinkedCellList(v);
        } else {
          head.add(v);
        }
    }

    public Cell getLast()
    //@ requires this.pred(?this_absVal_old) &*& !((this_absVal_old) == (nil));
    //@ ensures this.pred(?this_absVal) &*& (result) != (null) &*& (result) == (nth((length(this_absVal)) - (1), this_absVal));
    {
        Cell result; 
        if (head == null) { 
            result = null; 
        } else { 
            result = head.getLast(); 
        } 
        return result;
    }
}
