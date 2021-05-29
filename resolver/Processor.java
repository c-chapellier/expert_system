package resolver;

import model.*;
import java.util.*;

public class Processor {
    private Map<Operator, String> map = new TreeMap<>();
    private List<Node> allNodes = new ArrayList<>();

    public Processor(){
        map.put(Operator.AND, "and");
        map.put(Operator.OR, "or");
        map.put(Operator.XOR, "xor");
    }
    
    public List<Node> preprocess(List<Variable> vars, List<Rule> rules) throws Exception{
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < rules.size(); ++i){
            Node input = resolveInputCondition(rules.get(i).c1);
            resolveOutputCondition(input, rules.get(i).c2);
        }
        return nodes;
    }

    private Node addNodeToList(Node node){
        if (!allNodes.contains(node)){
            allNodes.add(node);
            return node;
        }
        return allNodes.get(allNodes.indexOf(node));
    }

    private Node addNodeToListOutput(Node node){
        if (!allNodes.contains(node)){
            allNodes.add(node);
            return node;
        } else {
            Node already = allNodes.get(allNodes.indexOf(node));
            if (already.p1 != null && already.p2 != null){
                Node newParent = new Node(concatName(already.p1.name, already.op, already.p2.name), already.p1, already.p2, already.op);
                if (allNodes.contains(newParent)){
                    already.p1 = newParent;
                    already.p2 = node.p1;
                    allNodes.add(newParent);
                }
            } else if (already.p1 != null && already.p2 == null){
                already.p2 = node.p2;
            } else {
                already.p1 = node.p1;
            }
            return already;
        }
    }

    private String concatName(String n1, Operator op, String n2){
        if (n1.compareTo(n2) < 0)
            return n1 + map.get(op) + n2;
        return n2 + map.get(op) + n1;
    }

    // Every input condition is made of either:
    // 2 conditions
    // 2 vars
    // 1 condition and 1 var
    // 1 var
    // this fonction create a map of all the input condition combined
    // Only and or xor for the moment
    private Node resolveInputCondition(Condition c) throws Exception {
        if (c.c1 != null && c.c2 != null){
            //two conditions
            Node p1 = resolveInputCondition(c.c1); // parent node
            Node p2 = resolveInputCondition(c.c2); // parent node
            Node node = new Node(concatName(p1.name, c.operator, p2.name), p1, p2, c.operator); // node for the condition
            return addNodeToList(node);
        } else if (c.c1 != null && c.v1 != null){
            //one condition and one var
            Node p1 = resolveInputCondition(c.c1); // parent node
            Node p2 = new Node("" + c.v1, null, null, Operator.AND); // parent node
            Node node = new Node(concatName(p1.name, c.operator, p2.name), p1, p2, c.operator); // node for the condition
            return addNodeToList(node);
        } else if (c.v1 != null && c.v2 != null){
            //two var
            Node p1 = new Node("" + c.v1, null, null, Operator.AND); // parent node
            Node p2 = new Node("" + c.v2, null, null, Operator.AND); // parent node
            Node node = new Node(concatName(p1.name, c.operator, p2.name), p1, p2, c.operator); // node for the condition
            return addNodeToList(node);
        } else if (c.v1 != null && c.v2 == null){ // last node of the three
            //one var
            Node p1 = new Node("" + c.v1, null, null, Operator.AND);
            return addNodeToList(p1);
        } else if (c.c1 != null && c.v1 == null){ // last node of the three
            // never reached
            //one condition
            Node p1 = resolveInputCondition(c.c1);
            return addNodeToList(p1);
        }
        throw new Exception("Never reached");
    }

    // Every output condition is made of either:
    // 2 conditions
    // 2 vars
    // 1 condition and 1 var
    // 1 var
    // this fonction create a map of all the input condition combined
    // Only and for the moment
    private void resolveOutputCondition(Node input, Condition c) throws Exception{
        if (c.c1 != null && c.c2 != null){
            //two conditions
            resolveOutputCondition(input, c.c1);
            resolveOutputCondition(input, c.c2);
        } else if (c.c1 != null && c.v1 != null){
            //one condition and one var
            resolveOutputCondition(input, c.c1);
            Node node = new Node("" + c.v1.name, input, null, Operator.AND);
            addNodeToListOutput(node);
        } else if (c.v1 != null && c.v2 != null){
            //two var
            Node node1 = new Node("" + c.v1.name, input, null, Operator.AND);
            Node node2 = new Node("" + c.v2.name, input, null, Operator.AND);
            addNodeToListOutput(node1);
            addNodeToListOutput(node2);
        } else if (c.v1 != null && c.v2 == null){ // last node of the three
            //one var
            Node node = new Node("" + c.v1.name, input, null, Operator.AND);
            addNodeToListOutput(node);
        } else if (c.c1 != null && c.v1 == null){ // last node of the three
            // never reached
            //one condition
            resolveOutputCondition(input, c.c1);
        }
        throw new Exception("Never reached");
    }
}
