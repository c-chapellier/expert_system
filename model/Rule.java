package model;

public class Rule {
    public Conditions reactants = null;
    public Conditions products = null;

    public Rule(String line) throws Exception {
        int index = line.indexOf("=>");
        if (index == -1) {
            throw new Exception("=> not found.");
        }
        this.reactants = new Conditions(line.substring(0, index));
        this.products = new Conditions(line.substring(index + 2));
    }

    @Override
    public String toString() {
        return "\t" + this.reactants.toString() + "\t=>\t" + this.products.toString();
    }
}
