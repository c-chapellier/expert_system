package parser;

import java.io.*;
import java.util.*;
import model.*;

public class Parser {

    //List des if and if
    private List<Rule> rules = null;
    //Variables avec leur état initiale
    private List<Variable> variables = null;
    //Variables à déterminer la valeur
    private List<Variable> queries = null;
    // path of file to parse
    private String path;
    
    public Parser(final List<Rule> rules, final List<Variable> variables, final List<Variable> queries, String path) {
        this.rules = rules;
        this.variables = variables;
        this.queries = queries;
        this.path = path;
    }

    public void parseVariables(String line) throws Exception {
        for (int i = 1; i < line.length(); ++i) {
            if (!Character.isUpperCase(line.charAt(i))) {
                throw new Exception("States must only be uppercase letters.");
            }
            this.variables.add(new Variable(line.charAt(i), State.UNDEFINED));
        }
    }

    public void parseQueries(String line) throws Exception {
        for (int i = 1; i < line.length(); ++i) {
            if (!Character.isUpperCase(line.charAt(i))) {
                throw new Exception("Queries must only be uppercase letters.");
            }
            this.queries.add(new Variable(line.charAt(i), State.UNDEFINED));
        }
    }

    public void parseLine(String line) throws Exception {
        int index = line.indexOf("#");
        if (index != -1) {
            line = line.substring(0, index);
        }
        if (line.isEmpty()) {
            // pass
        } else if (line.charAt(0) == '=') {
            this.parseVariables(line);
        } else if (line.charAt(0) == '?') {
            this.parseQueries(line);
        } else {
            System.out.println(line);
            line = line.replaceAll("[(][A-Q][)]", "$0"); // ici bg
            line = line.replaceAll("[(]![A-Q][)]", "$0");
            System.out.println(line);
            index = line.indexOf("<=>");
            if (index != -1) {
                String reactants = line.substring(0, index);
                String products = line.substring(index + 3);
                Rule rule = new Rule(reactants + "=>" + products);
                this.rules.add(rule);
                rule = new Rule(products + "=>" + reactants);
                this.rules.add(rule);
            } else {
                Rule rule = new Rule(line);
                this.rules.add(rule);
            }
        }
    }

    public void parseFile() throws Exception {
        File file = new File(this.path);
        Scanner scan = new Scanner(file);

        while (scan.hasNextLine()) {
            String line = scan.nextLine().replaceAll("\\s", "");
            try {
                this.parseLine(line);
            } catch (Exception E) {
                scan.close();
                throw E;
            }
        }
        scan.close();
    }
}
