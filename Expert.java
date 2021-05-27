import parser.*;
import model.*;
import java.util.*;

public class Expert {
    private static Data data;
    //Variables avec leur état initiale
    private static final List<Variable> variables = new ArrayList<>();
    //List des if and if
    private static final List<Imply> implies = new ArrayList<>();
    //Variables avec leur état de l'étape précédente
    private static List<Variable> previous = new ArrayList<>();
    //Variables avec leur état de l'étape actuel
    private static List<Variable> actual = new ArrayList<>();

    private static boolean goDeeper(){
        for (int i = 0; i < actual.size(); ++i){
            for (int j = 0; j < previous.size(); ++j){
                if( actual.get(i).name == previous.get(j).name &&
                    actual.get(i).state != previous.get(j).state)
                        return true;
            }
        }
        return false;
    }

    private static void solve(){
        //While a state of var change from previous state
        while(goDeeper()){
            //apply each implies

            //set previous state = current state
            //set current state = new state
        }
    }

    public static void main(String[] args){
        System.out.print("--------- Expert ----------\n");

        Parser parser = new Parser(args[0]);

        try {
            data = parser.parseFile();
        } catch (Exception e) {
            System.out.println("Exception -> " + e.getMessage());
        }

        for (int i = 0; i < data.rules.size(); ++i) {
            System.out.println("rule[" + i + "] = " + data.rules.get(i).toString());
        }
        System.out.println("initialStates = " + data.initialStates);
        System.out.println("queries = " + data.queries);
        System.out.print("--------- Expert ----------\n");
    }
}