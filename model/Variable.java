package model;

public class Variable {
    public char name;
    public State state;
    // !A
    public boolean isNot = false;

    public Variable(char name, State state){
        this.name = name;
        this.state = state;
    }

    public Variable(char name, State state, boolean isNot){
        this.name = name;
        this.state = state;
        this.isNot = isNot;
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
        String str = (this.isNot ? "!" : "") + name + ": " + state;
        return str;
    }
    
}
