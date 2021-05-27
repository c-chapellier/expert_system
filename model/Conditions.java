package model;

import java.util.*;

public class Conditions {

    String line = null;



    public Conditions(String line) throws Exception {
        this.line = line;
    }

    public List<State> call(List<State> states) throws Exception {

        int i = this.line.indexOf("(");
        if (i != -1) {
            int i2 = this.line.lastIndexOf(")");
            if (i2 == -1) {
                throw new Exception("')' character not found.");
            }
            // inside parentheses
        }
        throw new Exception("Nothing");
    }

    @Override
    public String toString() {
        return this.line;
    }
}
