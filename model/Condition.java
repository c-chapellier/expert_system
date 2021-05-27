package model;

public class Condition {
    public Variable v1;
    public Variable v2;
    public Condition c1;
    public Condition c2;
    public Operator operator;

    public Condition(Variable v1, Variable v2,Condition c1, Condition c2, Operator operator){
        this.v1 = v1;
        this.v2 = v2;
        this.c1 = c1;
        this.c2 = c2;
        this.operator = operator;
    }
}
