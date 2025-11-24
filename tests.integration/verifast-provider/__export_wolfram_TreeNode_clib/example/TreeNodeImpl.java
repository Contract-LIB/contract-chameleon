package example;

public class TreeNodeImpl extends TreeNode {

    //@ predicate pred(list<int> absVal);

    public void add(int v)
    //@ requires this.pred(?this_absVal_old) &*& true;
    //@ ensures this.pred(?this_absVal) &*& helper_predicate(v, this_absVal_old, v, this_absVal, ?helper_predicate_res) &*& (helper_predicate_res) && (METHOD_APPL);
    {
        //TODO: Implement method 'example.TreeNode.add'.
    }

    public boolean contains(int v)
    //@ requires this.pred(?this_absVal_old) &*& true;
    //@ ensures this.pred(?this_absVal) &*& (result) == (METHOD_APPL);
    {
        //TODO: Implement method 'example.TreeNode.contains'.
        return false;
    }
}
