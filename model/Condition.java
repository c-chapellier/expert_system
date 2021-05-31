package model;

import java.util.ArrayList;
import java.util.List;

// priority
// () ! + | ^ => <=>

public class Condition {
    public Variable v1 = null;
    public Variable v2 = null;
    public Condition c1 = null;
    public Condition c2 = null;
    public Operator operator = null;
    public boolean not1 = false;
    public boolean not2 = false;
    public boolean isNot = false;
    public List<Variable> input = new ArrayList<>();
    public List<Variable> output = new ArrayList<>();

    public Condition(Variable v1, Variable v2,Condition c1, Condition c2, Operator operator){
        this.v1 = v1;
        this.v2 = v2;
        this.c1 = c1;
        this.c2 = c2;
        this.operator = operator;
    }

    public Condition(String line) throws Exception {
        System.err.println("condition from: " + line);
        line = this.rmParentheses(line);
        System.err.println("rm parentheses: " + line);
        int deep = 0, xor = 0, or = 0/*, and = 0*/;
        for (int i = 0; i < line.length(); ++i) {
            if (line.charAt(i) == '(') {
                ++deep;
            } else if (line.charAt(i) == ')') {
                --deep;
            }
            if (deep == 0) {
                switch(line.charAt(i)) {
                    case '^':
                        ++xor;
                        break;
                    case '|':
                        ++or;
                        break;
                    case '+':
                        //++and;
                        break;
                }
            }
        }
        if (deep != 0) {
            throw new Exception("Bad parentheses.");
        }
        for (int i = 0; i < line.length(); ++i) {
            if (line.charAt(i) == '(') {
                ++deep;
            } else if (line.charAt(i) == ')') {
                --deep;
            }
            if (deep == 0) {
                switch(line.charAt(i)) {
                    case '^':
                        this.operator = Operator.XOR;
                        this.getCondOrVar(i, line);
                        break;
                    case '|':
                        if (xor == 0) {
                            this.operator = Operator.OR;
                            this.getCondOrVar(i, line);
                        }
                        break;
                    case '+':
                        if (xor == 0 && or == 0) {
                            this.operator = Operator.AND;
                            this.getCondOrVar(i, line);
                        }
                        break;
                }
            }
            if (this.operator != null) {
                break;
            }
        }
        // only a variable without operator
        if (this.operator == null) {
            this.v1 = new Variable(line.charAt(0), State.UNDEFINED);
        }
    }

    public String rmParentheses(String line) {
        if (line.charAt(0) == '!') {
            this.isNot = true;
            line = line.substring(1);
        }
        while (line.charAt(0) == '(' && line.charAt(line.length() - 1)== ')') {
            if (line.charAt(0) == '!') {
                this.isNot = true;
                line = line.substring(1);
                System.err.println("never happens");
            }
            int deep = 0;
            for (int i = 0; i < line.length() - 1; ++i) {
                if (line.charAt(i) == '(') {
                    ++deep;
                } else if (line.charAt(i) == ')') {
                    --deep;
                }
                if (deep == 0) {
                    deep = -1;
                    break;
                }
            }
            if (deep == -1) {
                break;
            }
            line = line.substring(1, line.length() - 1);
        }
        return line;
    }

    public void getCondOrVar(int i, String line) throws Exception {
        if (i == 1) {
            System.err.println("i == 1 [" + line.charAt(0) + "]");
            this.v1 = new Variable(line.charAt(0), State.UNDEFINED);
        } else {
            System.err.println("i != 1 [" + line.substring(0, i) + "]");
            this.c1 = new Condition(line.substring(0, i));
        }
        if (i == line.length() - 2) {
            System.out.println("i == size");
            if (this.v1 == null)
                this.v1 = new Variable(line.charAt(line.length() - 1), State.UNDEFINED);
            else
                this.v2 = new Variable(line.charAt(line.length() - 1), State.UNDEFINED);
        // } else if (i == line.length() - 3 && line.charAt(line.length() - 2) == '!') {
        //     System.out.println("i == size - 1");
        //     this.v1 = new Variable(line.charAt(line.length() - 1), State.UNDEFINED, true);
        } else {
            System.out.println("i != size");
            if (this.c1 == null)
                this.c1 = new Condition(line.substring(i + 1));
            else
                this.c2 = new Condition(line.substring(i + 1));
        }
    }

    @Override
    public String toString() {
        String str = this.isNot ? "!(" : "(";
        if (this.v1 != null) {
            str += this.v1.isNot ? "!" : "";
            str += this.v1.name;
        } else if (this.c1 != null) {
            str += this.c1.toString();
        } else {
            str += "houston we've got a problem";
        }
        if (this.operator != null)
        {
            switch (this.operator) {
                case XOR:
                    str += '^';
                    break;
                case OR:
                    str += '|';
                    break;
                case AND:
                    str += '+';
                    break;
            }
        }
        if (this.v2 != null) {
            str += this.v2.isNot ? "!" : "";
            str += this.v2.name;
        } else if (this.c2 != null) {
            str += this.c2.toString();
        }
        str += ")";
        return str;
    }
}
