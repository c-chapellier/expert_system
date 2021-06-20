package model;

import resolver.Resolver;

public class Node {
    public String name;
    public State state = State.FALSE;
    public Node p1;
    public Node p2;
    public Operator op;
    public boolean isNot = false;
    public int score = 0;
    public boolean fixed = false;

    public Node(String name, Node p1, Node p2, Operator op){
        this.name = name;
        this.p1 = p1;
        this.p2 = p2;
        this.op = op;
    }

    public int setScore(){
        if (p1 != null)
            ++score;
        if (p2 != null)
            ++score;
        return score;
    }

    public int updateScore(){
        score = 0;
        setScore();
        if (p1 != null && p1.fixed)
            --score;
        if (p2 != null && p2.fixed)
            --score;
        return score;
    }

    private String makeString(Node p1){
        String ret = "";
        ret += "If " + p1.name + "(" + p1.state + ")  => " + name + "(" + state + ")";
        return ret;
    }

    private String makeString(Node p1, Node p2){
        String ret = "";
        ret += "If " + p1.name + "(" + p1.state + ") " + op + " " + p2.name + "(" + p2.state + ") => " + name + "(" + state + ")";
        return ret;
    }

    public String updateState(){
        String ret = "";
        if (fixed) {
            //System.out.println(name + " is fixed");
            return ret;
        }
        if (p1 != null && p2 != null) {
            //System.out.println(name + " has two parents ");
            if (p1.fixed && p2.fixed) {
                //System.out.println("they are both fixed " + op);
                switch(op) {
                    case AND:
                        //System.out.println("AND");
                        state = Resolver.and(p1.state, p2.state);
                        if (isNot)
                            state = Resolver.not(state);
                        break;
                    case OR:
                        //System.out.println("OR");
                        state = Resolver.or(p1.state, p2.state);
                        if (isNot)
                            state = Resolver.not(state);
                        break;
                    case XOR:
                        //System.out.println("XOR");
                        state = Resolver.xor(p1.state, p2.state);
                        if (isNot)
                            state = Resolver.not(state);
                        break;
                }
                ret += makeString(p1, p2);
                fixed = true;
            } else if (p1.fixed || p2.fixed){
                //System.out.println("only one is fixed");
                if (op == Operator.OR){
                    state = Resolver.or(p1.state, p2.state);
                    if (isNot)
                        state = Resolver.not(state);
                    ret += makeString(p1, p2);
                    fixed = true;
                }
            }
        } else if (p1 != null || p2 != null) {
            //System.out.println(name + " has one parents");
            Node tmp = p1 != null ? p1 : p2;
            if (tmp.fixed) {
                //System.out.println("and he is fixed");
                state = tmp.state;
                if (isNot)
                    state = Resolver.not(state);
                ret += makeString(tmp);
                fixed = true;
            }
        } else {
            fixed = true;
        }
        return ret;
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
        str += "addr: " + System.identityHashCode(this) + "\n";
        str += "name: " + name + "\n";
        str += "p1: " + (p1 != null ? p1.name + " at " + System.identityHashCode(p1) : "null") + "\n";
        str += "p2: " + (p2 != null ? p2.name + " at " + System.identityHashCode(p2) : "null") + "\n";
        str += "fixed: " + fixed + "\n";
        str += "isNot: " + isNot + "\n";
        str += "state: " + state;
        return str;
    }
}
