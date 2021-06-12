package de.syngenio.model;

public class Variable {
    public char name;
    public State state;
    public boolean fix = false;

    public Variable(char name, State state){
        this.name = name;
        this.state = state;
    }

    public void setState(State s) throws Exception{
        if ((state == State.FALSE && s == State.TRUE) ||
            (state == State.TRUE && s == State.FALSE)){
            throw new Exception("A variable can't be both true and false");
        } else if (s == State.UNDEFINED){
            //PASS
        } else {
            state = s;
        }
    }

    @Override
    public boolean equals(Object o){
        if (o == null)
            return false;
        if (!(o instanceof Variable))
            return false;
        Variable var = (Variable)o;
        return (name == var.name);
    }

    @Override
    public String toString(){
        String str = name + ": " + state;
        return str;
    }
    
}
