
import parser.*;
import model.*;

public class Expert {
    private static Data data;

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