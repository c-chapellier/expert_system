package model;

import java.util.*;

import resolver.Resolver;

public class Node {
    public String name;
    public State state = State.FALSE;
    public Node p1;
    public Node p2;
    public Operator op;
    public boolean not1 = false;
    public boolean not2 = false;
    public List<Node> childs = new ArrayList<>();
    public int score = 0;
    public boolean fixed = false;

    public Node(String name, Node p1, Node p2, Operator op){
        this.name = name;
        this.p1 = p1;
        this.p2 = p2;
        this.op = op;
    }

    public void addChild(Node c){
        if (!childs.contains(c))
            childs.add(c);
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
        if (fixed)
            return ret;
        if (p1 != null && p2 != null) {
            //System.out.println(name + " has two parents ");
            if (p1.fixed && p2.fixed) {
                //System.out.println("they are both fixed " + op);
                switch(op) {
                    case AND:
                        //System.out.println("AND");
                        if(!not1 && !not2)
                            state = Resolver.and(p1.state, p2.state);
                        else if (!not1 && not2)
                            state = Resolver.and(p1.state, Resolver.not(p2.state));
                        else if (not1 && !not2)
                            state = Resolver.and(Resolver.not(p1.state), p2.state);
                        else
                            state = Resolver.and(Resolver.not(p1.state), Resolver.not(p2.state));
                        break;
                    case OR:
                        //System.out.println("OR");
                        if(!not1 && !not2)
                            state = Resolver.or(p1.state, p2.state);
                        else if (!not1 && not2)
                            state = Resolver.or(p1.state, Resolver.not(p2.state));
                        else if (not1 && !not2)
                            state = Resolver.or(Resolver.not(p1.state), p2.state);
                        else
                            state = Resolver.or(Resolver.not(p1.state), Resolver.not(p2.state));
                        break;
                    case XOR:
                        //System.out.println("XOR");
                        if(!not1 && !not2)
                            state = Resolver.xor(p1.state, p2.state);
                        else if (!not1 && not2)
                            state = Resolver.xor(p1.state, Resolver.not(p2.state));
                        else if (not1 && !not2)
                            state = Resolver.xor(Resolver.not(p1.state), p2.state);
                        else
                            state = Resolver.xor(Resolver.not(p1.state), Resolver.not(p2.state));
                        break;
                }
                ret += makeString(p1, p2);
                fixed = true;
            } else if (p1.fixed || p2.fixed){
                System.out.println("only one is fixed");
                if (op == Operator.OR){
                    if(!not1 && !not2)
                        state = Resolver.or(p1.state, p2.state);
                    else if (!not1 && not2)
                        state = Resolver.or(p1.state, Resolver.not(p2.state));
                    else if (not1 && !not2)
                        state = Resolver.or(Resolver.not(p1.state), p2.state);
                    else
                        state = Resolver.or(Resolver.not(p1.state), Resolver.not(p2.state));
                    ret += makeString(p1, p2);
                    fixed = true;
                }
            }
        } else if (p1 != null && p2 == null) {
            System.out.println(name + " has one parents");
            if (p1.fixed){
                System.out.println("and he is fixed");
                if (not1)
                    state = Resolver.not(p1.state);
                else
                    state = p1.state;
                ret += makeString(p1);
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
        String str = not1 + " " + not2;
        str += name + "\n";
        if (p1 != null)
            str += p1.toString() + "\n";
        if (p2 != null)
            str += p2.toString()  + "\n";
        return str;
    }
}
