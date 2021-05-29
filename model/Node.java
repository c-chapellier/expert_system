package model;

import java.util.*;

import resolver.Resolver;

public class Node {
    public String name;
    public State state = State.FALSE;
    public Node p1;
    public Node p2;
    public Operator op;
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

    public void updateState(){
        if (fixed)
            return;
        if (p1 != null && p2 != null) {
            System.out.println(name + " has two parents ");
            if (p1.fixed && p2.fixed) {
                System.out.println("they are both fixed " + op);
                switch(op) {
                    case AND:
                        System.out.println("AND");
                        state = Resolver.and(p1.state, p2.state);
                        break;
                    case OR:
                        System.out.println("OR");
                        state = Resolver.or(p1.state, p2.state);
                        System.out.println(state);
                        break;
                    case XOR:
                        System.out.println("XOR");
                        state = Resolver.xor(p1.state, p2.state);
                        break;
                }
                fixed = true;
            } else if (p1.fixed || p2.fixed){
                System.out.println("only one is fixed");
                if (op == Operator.OR){
                    state = Resolver.or(p1.state, p2.state);
                    fixed = true;
                }
            }
        } else if (p1 != null && p2 == null) {
            System.out.println(name + " has one parents");
            if (p1.fixed){
                System.out.println("and he is fixed");
                state = p1.state;
                fixed = true;
            }
        } else {
            fixed = true;
        }
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
