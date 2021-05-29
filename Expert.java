import parser.*;
import resolver.Processor;
import model.*;
import java.util.*;

public class Expert {
    //List des if and if
    private static final List<Rule> rules = new ArrayList<>();
    //Variables avec leur état initiale
    private static final List<Variable> variables = new ArrayList<>();
    //Variables à déterminer la valeur
    private static final List<Variable> queries = new ArrayList<>();
    //The graph
    private static List<Node> nodes;

    private static void outputFinalStates(){
        for (int i = 0; i < nodes.size(); ++i){
            if (nodes.get(i).name.length() == 1)
                System.out.println(nodes.get(i).name + " : " + nodes.get(i).state);
        }
    }

    private static void addChildToNode(){
        for (int i = 0; i < nodes.size(); ++i){
            Node node = nodes.get(i);
            if (node.p1 != null){
                node.p1.addChild(node);
            }
            if (node.p2 != null){
                node.p2.addChild(node);
            }
        }
    }

    private static int setScoreToNode(){
        int score = 0;
        for (int i = 0; i < nodes.size(); ++i){
            score += nodes.get(i).setScore();
        }
        return score;
    }

    private static int updateScoreToNode(){
        int score = 0;
        for (int i = 0; i < nodes.size(); ++i){
            score += nodes.get(i).updateScore();
        }
        return score;
    }

    private static void setFixedNode(){
        for (int i = 0; i < nodes.size(); ++i){
            Node node = nodes.get(i);
            node.state = State.UNDEFINED;
            node.fixed = false;
            for (int j = 0; j < variables.size(); ++j){
                if(node.name.compareTo("" + variables.get(j).name) == 0){
                    node.state = variables.get(j).state;
                    node.fixed = true;
                }
            }
        }
    }

    private static void updateStateNode(){
        for (int i = 0; i < nodes.size(); ++i){
            nodes.get(i).updateState();
        }
    }

    private static void solveForward(){
        int previousScore, actualScore;
        // 0 step : add child for each node
        addChildToNode();
        // 2 step : set fixed nodes
        setFixedNode();
        // 3 step : set the score for all nodes # A B C ...
        previousScore = setScoreToNode();
        actualScore = updateScoreToNode();
        System.out.println(previousScore + " " + actualScore);
        
        // 4 step : for each known node, decrement the score from other nodes from one if they come from it
        while(previousScore > actualScore) {
            System.out.println(previousScore + " " + actualScore);
            previousScore = actualScore;
            updateStateNode();
            actualScore = updateScoreToNode();
        }
    }

    private static Node getNode(Variable v) throws Exception{
        for (int i = 0; i < nodes.size(); ++i){
            if (nodes.get(i).name.compareTo("" + v.name) == 0)
                return nodes.get(i);
        }
        throw new Exception ("Never happen");
    }

    private static void solveB(Node node){
        if (node.p1 != null){
            solveB(node.p1);
        }
        if (node.p2 != null){
            solveB(node.p2);
        }
        node.updateState();
    }

    private static void solveBackward() throws Exception{
        // 0 step : add child for each node
        addChildToNode();
        // 2 step : set fixed nodes
        setFixedNode();
        for (int i = 0; i < queries.size(); ++i) {
            Node n = getNode(queries.get(i));
            solveB(n);
        }
    }

    // Convert the fatcs and the rule into a graph
    private static void preProcessing() throws Exception{
        nodes = new Processor().preprocess(variables, rules);
        System.out.println("Size: " + nodes.size());
        System.out.println("#####################################");
        for (int i = 0; i < nodes.size(); ++i){
            System.out.println(nodes.get(i));
            System.out.println("* * * * * * * * * *");
        }
    }

    public static void main(String[] args){
        System.out.print("--------- Expert ----------\n");
        Parser parser = new Parser(rules, variables, queries, args[0]);

        try {
            parser.parseFile();
            preProcessing();
            //solveForward();
            solveBackward();
            System.out.println("END");
            outputFinalStates();
        } catch (Exception e) {
            System.out.println("Exception -> " + e.getMessage());
        }

        //for (int i = 0; i < rules.size(); ++i) {
            //System.out.println("rule[" + i + "] = " + rules.get(i).toString());
        //}
        //for (int i = 0; i < variables.size(); ++i) {
            //System.out.println("variable[" + i + "] = " + variables.get(i).toString());
        //}
        //for (int i = 0; i < queries.size(); ++i) {
            //System.out.println("query[" + i + "] = " + queries.get(i).toString());
        //}
        //System.out.print("--------- Expert ----------\n");
    }
}