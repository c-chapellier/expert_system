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
    // //Variables avec leur état de l'étape précédente
    private static List<Node> nodes;
    // private static List<Variable> previous = new ArrayList<>();
    // //Variables avec leur état de l'étape actuel
    // private static List<Variable> actual = new ArrayList<>();

    // private static boolean goDeeper(){
    //     for (int i = 0; i < actual.size(); ++i){
    //         for (int j = 0; j < previous.size(); ++j){
    //             if( actual.get(i).name == previous.get(j).name &&
    //                 actual.get(i).state != previous.get(j).state)
    //                     return true;
    //         }
    //     }
    //     return false;
    // }

    // private static void solve(){
    //     //While a state of var change from previous state
    //     while(goDeeper()){
    //         //apply each implies

    //         //set previous state = current state
    //         //set current state = new state
    //     }
    // }

    // Convert the fatcs and the rule into a graph
    private static void preProcessing() throws Exception{
        nodes = new Processor().preprocess(variables, rules);
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
        } catch (Exception e) {
            System.out.println("Exception -> " + e.getMessage());
        }

        for (int i = 0; i < rules.size(); ++i) {
            System.out.println("rule[" + i + "] = " + rules.get(i).toString());
        }
        for (int i = 0; i < variables.size(); ++i) {
            System.out.println("variable[" + i + "] = " + variables.get(i).toString());
        }
        for (int i = 0; i < queries.size(); ++i) {
            System.out.println("query[" + i + "] = " + queries.get(i).toString());
        }
        System.out.print("--------- Expert ----------\n");
    }
}