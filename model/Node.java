package model;

public class Node {
    public String name;
    public State state = State.FALSE;
    public Node p1;
    public Node p2;
    public Operator op;

    public Node(String name, Node p1, Node p2, Operator op){
        this.name = name;
        this.p1 = p1;
        this.p2 = p2;
        this.op = op;
    }

    @Override
    public boolean equals(Object o){
        if (o == null)
            return false;
        if (!(o instanceof Node))
            return false;
        Node node = (Node)o;
        return name.compareTo(node.name) == 0;
    }

    @Override
    public String toString(){
        String str = "";
        str += name + "\n";
        if (p1 != null)
            str += p1.toString() + "\n";
        if (p2 != null)
            str += p2.toString()  + "\n";
        return str;
    }
}
