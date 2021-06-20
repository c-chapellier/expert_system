import parser.*;
import resolver.Processor;
import model.*;
import java.util.*;

public class Expert {
    private static Method method; // forward or backward
    private static int timeComplexity;
    private static int spaceComplexity;
    private static String explanation = "";
    private static Operator defaultOperator = Operator.AND;
    private static State defaultState = State.UNDEFINED;
    //List des if and if
    private static List<Rule> rules = new ArrayList<>();
    //Variables avec leur état initiale
    private static List<Variable> variables = new ArrayList<>();
    //Variables à déterminer la valeur
    private static List<Variable> queries = new ArrayList<>();
    //The graph
    private static List<Node> nodes;

    private static void ouputComplexity(){
        System.out.println("Space complexity: " + spaceComplexity);
        System.out.println("Time complexity: " + timeComplexity);
    }

    private static void outputExplanation(){
        String[] args = explanation.split("\\n");
        explanation = "";
        System.out.println(args.length);
        for(int i = 0; i < args.length; ++i) {
            if(!args[i].isEmpty())
                explanation += args[i] + "\n";
        }
        System.out.println(explanation);
    }

    private static void outputFinalStates(){
        for (int i = 0; i < nodes.size(); ++i){
            if (nodes.get(i).name.length() == 1)
                System.out.println(nodes.get(i).name + " : " + nodes.get(i).state);
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
            node.state = defaultState;
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
        // 3 step : set the score for all nodes # A B C ...
        previousScore = setScoreToNode();
        actualScore = updateScoreToNode();
        
        // 4 step : for each known node, decrement the score from other nodes from one if they come from it
        while(previousScore > actualScore) {
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

    // what a good name man
    private static void solveB(Node node){
        //System.out.print("\033[91msolve[" + node.name + "] at + " + System.identityHashCode(node) + " \033[0m");
        //System.out.print("[" + (node.p1 != null ? node.p1.name + " at " + System.identityHashCode(node.p1) : "null") + "]");
        //System.out.println("[" + (node.p2 != null ? node.p2.name + " at " + System.identityHashCode(node.p2) : "null") + "]");
        if (node.p1 != null && !node.p1.fixed){
            //System.out.print("1: ");
            solveB(node.p1);
        }
        if (node.p2 != null && !node.p2.fixed){
            //System.out.print("2: ");
            solveB(node.p2);
        }
        // System.out.println("----- " + node.name + " -----");
        // for (int i = 0; i < nodes.size(); ++i) {
        //     System.out.println(nodes.get(i).name + ": " + nodes.get(i).state);
        // }
        // System.out.println("----------------");
        String tmp = node.updateState();
        //System.out.println("\033[91mexplanation\033[0m[" + tmp + "]");
        //System.out.println("\033[91mstate\033[0m[" + node.state + "]");

        explanation += tmp + "\n";
        ++timeComplexity;
    }

    private static void solveBackward() throws Exception{
        for (int i = 0; i < queries.size(); ++i) {
            Node n = getNode(queries.get(i));
            solveB(n);
        }
    }

    private static void solve(String[]args) throws Exception{
        spaceComplexity = nodes.size();
        switch(method){
            case FORWARD:
                solveForward();
            case BACKWARD:
                solveBackward();
        }
        ouputComplexity();
        outputExplanation();
        outputFinalStates();
    }

    // Convert the facts and the rule into a graph
    private static void preProcessing() throws Exception{
        nodes = new Processor(defaultOperator).preprocess(rules);
        setFixedNode();
    }

    private static void checkArgs(String[] args) throws Exception {
        if (args.length != 4){
            System.out.println( "usage: java expert <file> <method {forward, backward} > <default state {false, undefined} > <default link {or, and} >");
            System.exit(1);
        }

        if (args[1].compareTo("forward") == 0)
            method = Method.FORWARD;
        else if (args[1].compareTo("backward") == 0)
            method = Method.BACKWARD;
        else {
            throw new Exception("Bad method.");
        }

        if (args[2].compareTo("false") == 0)
            defaultState = State.FALSE;
        else if (args[2].compareTo("undefined") == 0)
            defaultState = State.UNDEFINED;
        else {
            throw new Exception("Bad state.");
        }

        if (args[3].compareTo("or") == 0)
            defaultOperator = Operator.OR;
        else if (args[3].compareTo("and") == 0)
            defaultOperator = Operator.AND;
        else {
            throw new Exception("Bad link.");
        }
    }

    public static String checkInput(String str){
        str = str.replaceAll("\\s+", "");
        return str;
    }

    public static void parseInput(String line) throws Exception {
        for (int i = 1; i < line.length(); ++i) {
            if (!Character.isUpperCase(line.charAt(i))) {
                throw new Exception("States must only be uppercase letters.");
            }
            variables.add(new Variable(line.charAt(i), State.TRUE));
        }
    }

    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        try {
            checkArgs(args);
            while(true){
                rules = new ArrayList<>();
                queries = new ArrayList<>();

                String input = scan.nextLine();
                input = checkInput(input);

                if (input.toLowerCase().compareTo("exit") == 0)
                    System.exit(0);
                Parser parser = new Parser(rules, variables, queries, args[0]);
                parser.parseFile();

                variables = new ArrayList<>();
                parseInput(input);

                /*for (int i = 0; i < rules.size(); ++i) {
                    System.out.println("rule[" + i + "] = " + rules.get(i).toString());
                }
                for (int i = 0; i < variables.size(); ++i) {
                    System.out.println("variable[" + i + "] = " + variables.get(i).toString());
                }
                for (int i = 0; i < queries.size(); ++i) {
                    System.out.println("query[" + i + "] = " + queries.get(i).toString());
                }
                System.out.print("------- Parsing --------\n");
                System.out.print("------- Solving --------\n");
                */
                preProcessing();
                solve(args);
            }

        } catch (Exception e) {
            System.out.println("Exception -> " + e.getMessage());
            scan.close();
        }
        scan.close();
    }
}