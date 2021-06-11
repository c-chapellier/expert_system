package test.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import main.parser.*;
import main.model.*;

import org.junit.jupiter.api.Test;

public class TestController {

    @Test
    void ex2Test() {
        List<Rule> rules = new ArrayList<>();
        List<Variable> variables = new ArrayList<>();
        List<Variable> queries = new ArrayList<>();
        Parser parser = new Parser(rules, variables, queries, "../../inputs/ex2");
        try {
            parser.parseFile();
        } catch (Exception e) {
            System.out.println("Exception -> " + e.getMessage());
        }
        String output = "";
        for (int i = 0; i < rules.size(); ++i) {
            output += "rule[" + i + "] = " + rules.get(i).toString() + "\n";
        }
        for (int i = 0; i < variables.size(); ++i) {
            output += "variable[" + i + "] = " + variables.get(i).toString() + "\n";
        }
        for (int i = 0; i < queries.size(); ++i) {
            output += "query[" + i + "] = " + queries.get(i).toString() + "\n";
        }
        String checked = "rule[0] =       (A+X)   =>      (B)\nrule[1] =       (B)     =>      (C)\nrule[2] =       !(C)    =>      (D)\nvariable[0] = A: TRUE\nvariable[1] = X: TRUE\nquery[0] = D: UNDEFINED\n";
        assertEquals(checked, output);
    }
    
}
