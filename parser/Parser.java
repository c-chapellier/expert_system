package parser;

import java.io.*;
import java.util.*;
import model.*;

public class Parser {

    private String path;
    
    public Parser(String path) {
        this.path = path;
    }

    public String parseInitialStates(String line) throws Exception {
        for (int i = 1; i < line.length(); ++i) {
            if (!Character.isUpperCase(line.charAt(i))) {
                throw new Exception("States must only be uppercase letters.");
            }
        }
        return line.substring(1);
    }

    public String parseQueries(String line) throws Exception {
        for (int i = 1; i < line.length(); ++i) {
            if (!Character.isUpperCase(line.charAt(i))) {
                throw new Exception("Queries must only be uppercase letters.");
            }
        }
        return line.substring(1);
    }

    public void parseLine(Data data, String line) throws Exception {
        
        int index = line.indexOf("#");
        if (index != -1) {
            line = line.substring(0, index);
        }

        if (line.isEmpty()) {
            // pass
        } else if (line.charAt(0) == '=') {
            data.initialStates = this.parseInitialStates(line);
        } else if (line.charAt(0) == '?') {
            data.queries = this.parseQueries(line);
        } else {
            index = line.indexOf("<=>");
            if (index != -1) {
                String reactants = line.substring(0, index);
                String products = line.substring(index + 3);
                Rule rule = new Rule(reactants + "=>" + products);
                data.rules.add(rule);
                rule = new Rule(products + "=>" + reactants);
                data.rules.add(rule);
            } else {
                Rule rule = new Rule(line);
                data.rules.add(rule);
            }
        }
    }

    public Data parseFile() throws Exception {
        Data data = new Data();
        File file = new File(path);
        Scanner scan = new Scanner(file);

        while (scan.hasNextLine()) {
            String line = scan.nextLine().replaceAll("\\s", "");
            try {
                this.parseLine(data, line);
            } catch (Exception E) {
                scan.close();
                throw E;
            }
        }
        scan.close();
        return data;
    }
}
