package model;

public class Imply {
    public Condition c1;
    public Condition c2;
    public Implication implication;

    public Imply(Condition c1, Condition c2, Implication implication){
        this.c1 = c1;
        this.c2 = c2;
        this.implication = implication;
    }
}
