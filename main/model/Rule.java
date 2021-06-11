package main.model;

public class Rule {
    public Condition c1;
    public Condition c2;

    public Rule(Condition c1, Condition c2){
        this.c1 = c1;
        this.c2 = c2;
    }

    public Rule(String line) throws Exception {
        int index = line.indexOf("=>");
        if (index == -1) {
            throw new Exception("=> not found.");
        }
        this.c1 = new Condition(line.substring(0, index));
        this.c2 = new Condition(line.substring(index + 2));
    }

    @Override
    public String toString() {
        return "\t" + this.c1.toString() + "\t=>\t" + this.c2.toString();
    }
}
