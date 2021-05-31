import parser.*;
import resolver.Processor;
import model.*;
import java.util.*;

public class Expert {
    private static Methode methode; // forward or backward
    private static int timeComplexity;
    private static int spaceComplexity;
    private static String explanation = "";
    //List des if and if
    private static final List<Rule> rules = new ArrayList<>();
    //Variables avec leur état initiale
    private static final List<Variable> variables = new ArrayList<>();
    //Variables à déterminer la valeur
    private static final List<Variable> queries = new ArrayList<>();
    //The graph
    private static List<Node> nodes;

    private static void ouputComplexity(){
        System.out.println("Space complexity: " + spaceComplexity);
        System.out.println("Time complexity: " + timeComplexity);
    }

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
            explanation += nodes.get(i).updateState();
            explanation += "\n";
            ++timeComplexity;
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
        explanation += node.updateState();
        explanation += "\n";
        ++timeComplexity;
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

    private static void solve(String[]args) throws Exception{
        spaceComplexity = nodes.size();
        switch(methode){
            case FORWARD:
                solveForward();
            case BACKWARD:
                solveBackward();
        }
        ouputComplexity();
        System.out.println(explanation);
        outputFinalStates();
    }

    // Convert the fatcs and the rule into a graph
    private static void preProcessing() throws Exception{
        nodes = new Processor().preprocess(rules);
        /*
        System.out.println("Size: " + nodes.size());
        System.out.println("#####################################");
        for (int i = 0; i < nodes.size(); ++i){
            System.out.println(nodes.get(i));
            System.out.println("* * * * * * * * * *");
        }
        */
    }

    private static void checkArgs(String[]args){
        if (args.length != 2){
            System.out.println( "ARsg needed : <file> <methode>\n" +
                                "\tmethode:\n" +
                                "\t\tforward\n" +
                                "\t\tbackward\n"
            );
            System.exit(1);
        }
        if (args[1].compareTo("forward") == 0)
            methode = Methode.FORWARD;
        else if (args[1].compareTo("backward") == 0)
            methode = Methode.BACKWARD;
    }

    public static void main(String[] args){
        checkArgs(args);
        System.out.print("--------- Expert ----------\n");
        Parser parser = new Parser(rules, variables, queries, args[0]);

        System.out.print("------- Parsing --------\n");
        try {

            parser.parseFile();
            preProcessing();
            solve(args);
        } catch (Exception e) {
            System.out.println("Exception -> " + e.getMessage());
        }
        System.out.print("------- Parsing --------\n");

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