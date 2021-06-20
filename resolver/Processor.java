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
    
    public List<Node> preprocess(List<Rule> rules) throws Exception{
        for (int i = 0; i < rules.size(); ++i){
            Node input = resolveInputCondition(rules.get(i).c1);
            resolveOutputCondition(input, rules.get(i).c2);
        }
        return allNodes;
    }

    private String concatName(String n1, Operator op, String n2){
        if (n1.compareTo(n2) < 0)
            return n1 + map.get(op) + n2;
        return n2 + map.get(op) + n1;
    }

    private Node addNodeToList(Node node){
        if (!allNodes.contains(node)){
            allNodes.add(node);
            return node;
        }
        return allNodes.get(allNodes.indexOf(node));
    }

    private Node addNodeToListOutput(Node node) throws Exception {
        if (!allNodes.contains(node)){
            allNodes.add(node);
            return node;
        } else {
            Node already = allNodes.get(allNodes.indexOf(node));
            if (already.p1 != null && already.p2 != null){
                System.out.println("TWO PARENT: " + node.name);
                Node newParent = new Node(concatName(already.p1.name, already.op, already.p2.name), already.p1, already.p2, already.op);
                if (allNodes.contains(newParent)){
                    Node tmp = already.p1;
                    already.p1 = newParent;
                    already.p2 = tmp;
                    allNodes.add(newParent);
                }
            } else if (already.p1 != null){
                System.out.println("ONE PARENT: " + node.name);
                already.p2 = node.p1;
                already.op = Operator.AND;
            } else if (already.p2 != null){
                System.out.println("ONE PARENT: " + node.name);
                already.p1 = node.p1;
                already.op = Operator.AND;
            } else {
                throw new Exception("Unacceptable");
            }
            return already;
        }
    }

    // this fonction create a map of all the input condition combined
    private Node resolveInputCondition(Condition c) throws Exception {
        System.out.println(c);
        if (c.c1 != null && c.c2 != null){
            //two conditions
            System.out.println("TWO CONDITIONS");
            Node node = null;
            Node p1 = resolveInputCondition(c.c1); // parent node
            Node p2 = resolveInputCondition(c.c2); // parent node
            addNodeToList(p1);
            addNodeToList(p2);
            if (c.isNot){
                node = new Node("not" + concatName(p1.name, c.operator, p2.name), p1, p2, c.operator);
                node.isNot = true;
            } else {
                node = new Node(concatName(p1.name, c.operator, p2.name), p1, p2, c.operator);
            }
            return addNodeToList(node);
        } else if (c.c2 != null && c.v1 != null){
            //one condition and one var
            System.out.println("ONE CONDITIONS - ONE VAR");
            Node node = null;
            Node p1 = resolveInputCondition(c.c2); // parent node
            Node p2 = new Node("" + c.v1.name, null, null, Operator.AND); // parent node
            addNodeToList(p1);
            addNodeToList(p2);
            // isNot
            if (c.isNot){
                node = new Node("not" + concatName(p1.name, c.operator, p2.name), p1, p2, c.operator);
                node.isNot = true;
            } else {
                node = new Node(concatName(p1.name, c.operator, p2.name), p1, p2, c.operator);
            }
            return addNodeToList(node);
        } else if (c.c1 != null && c.v2 != null){
            //one condition and one var
            System.out.println("ONE CONDITIONS - ONE VAR");
            Node node = null;
            Node p1 = resolveInputCondition(c.c1); // parent node
            Node p2 = new Node("" + c.v2.name, null, null, Operator.AND); // parent node
            addNodeToList(p1);
            addNodeToList(p2);
            // isNot
            if (c.isNot){
                node = new Node("not" + concatName(p1.name, c.operator, p2.name), p1, p2, c.operator);
                node.isNot = true;
            } else {
                node = new Node(concatName(p1.name, c.operator, p2.name), p1, p2, c.operator);
            }
            return addNodeToList(node);
        } else if (c.v1 != null && c.v2 != null){
            //two var
            System.out.println("TWO VARS");
            Node node = null;
            Node p1 = new Node("" + c.v1.name, null, null, Operator.AND); // parent node
            Node p2 = new Node("" + c.v2.name, null, null, Operator.AND); // parent node
            addNodeToList(p1);
            addNodeToList(p2);
            //isNot
            if (c.isNot){
                node = new Node("not" + concatName(p1.name, c.operator, p2.name), p1, p2, c.operator);
                node.isNot = true;
            } else {
                node = new Node(concatName(p1.name, c.operator, p2.name), p1, p2, c.operator);
            }
            return addNodeToList(node);
        } else if (c.v1 != null && c.v2 == null){ // last node of the three
            //one var
            System.out.println("ONE VAR");
            Node p1 = new Node("" + c.v1.name, null, null, Operator.AND);
            addNodeToList(p1);
            if (c.isNot){
                Node node = new Node("not" + c.v1.name, p1, null, Operator.AND);
                node.isNot = true;
                return addNodeToList(node);
            }
            return addNodeToList(p1);
        } else if (c.c1 != null && c.v1 == null){ // last node of the three
            //one condition
            throw new Exception("one condition");
            // System.out.println("ONE CONDITION");
            // Node p1 = resolveInputCondition(c.c1);
            // addNodeToList(p1);
            // //add isNot
            // if (c.isNot){
            //     Node node = new Node("not" + c.v1.name, p1, null, Operator.AND);
            //     node.isNot = true;
            //     return addNodeToList(node);
            // }
        }
        System.out.println(c.c1);
        System.out.println(c.c2);
        System.out.println(c.v1);
        System.out.println(c.v2);
        throw new Exception("Never reached input" + c);
    }

    // this function create a map of all the input condition combined
    private void resolveOutputCondition(Node input, Condition c) throws Exception {
        if (c.c1 != null && c.c2 != null){
            //two conditions
            resolveOutputCondition(input, c.c1);
            resolveOutputCondition(input, c.c2);
        } else if (c.c2 != null && c.v1 != null){
            //one condition and one var
            resolveOutputCondition(input, c.c2);
            Node node = new Node("" + c.v1.name, input, null, Operator.AND);
            if(c.isNot){
                node.isNot = !node.isNot;
            }
            addNodeToListOutput(node);
        } else if (c.c1 != null && c.v2 != null){
            //one condition and one var
            resolveOutputCondition(input, c.c1);
            Node node = new Node("" + c.v2.name, input, null, Operator.AND);
            if(c.isNot){
                node.isNot = !node.isNot;
            }
            addNodeToListOutput(node);
        } else if (c.v1 != null && c.v2 != null){
            //two var
            Node node1 = new Node("" + c.v1.name, input, null, Operator.AND);
            Node node2 = new Node("" + c.v2.name, input, null, Operator.AND);
            if(c.isNot) {
                node1.isNot = !node1.isNot;
                node2.isNot = !node2.isNot;
                throw new Exception("inverser les variables c'est pas equivalent mon gars");
            }
            addNodeToListOutput(node1);
            addNodeToListOutput(node2);
        } else if (c.v1 != null || c.v2 != null){ // last node of the three
            //one var
            Node node = new Node("" + (c.v1 == null ? c.v2.name : c.v1.name), input, null, Operator.AND);
            if(c.isNot)
                node.isNot = true;
            addNodeToListOutput(node);
        } else {
            throw new Exception("Something got wrong");
        }
    }
}
