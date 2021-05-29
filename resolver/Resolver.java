package resolver;
import model.*;

public class Resolver {

    //porte logic NOT
    public static State not(State state) {
        if (state == State.FALSE) {
            return State.TRUE;
        } else if (state == State.TRUE) {
            return State.FALSE;
        } else {
            return State.UNDEFINED;
        }
    }

    //porte logic AND 
    public static State and(State state1, State state2){
        if(state1 == State.TRUE && state2 == State.TRUE){
            return State.TRUE;
        } else if (state1 == State.FALSE || state2 == State.FALSE) {
            return State.FALSE;
        } else {
            return State.UNDEFINED;
        }
    }

    //porte logic OR
    public static State or(State state1, State state2){
        if(state1 == State.TRUE || state2 == State.TRUE){
            return State.TRUE;
        } else if (state1 == State.FALSE && state2 == State.FALSE) {
            return State.FALSE;
        } else {
            return State.UNDEFINED;
        }
    }

    //porte logic XOR
    public static State xor(State state1, State state2){
        if( (state1 == State.TRUE && state2 == State.FALSE) ||
            (state1 == State.FALSE && state2 == State.TRUE)){
            return State.TRUE;
        } else if ( (state1 == State.FALSE && state2 == State.FALSE) ||
                    (state1 == State.TRUE && state2 == State.TRUE)) {
            return State.FALSE;
        } else {
            return State.UNDEFINED;
        }
    }

    public State resolve(Condition c) throws Exception{
        if (c.c1 != null && c.c2 != null){
            //two conditions
            switch(c.operator) {
                case AND:
                    return Resolver.and(resolve(c.c1), resolve(c.c2));
                case OR:
                    return Resolver.or(resolve(c.c1), resolve(c.c2));
                case XOR:
                    return Resolver.xor(resolve(c.c1), resolve(c.c2));
            }
        } else if (c.c1 != null && c.v1 != null){
            //one condition and one var
            switch(c.operator) {
                case AND:
                    return Resolver.and(resolve(c.c1), c.v1.state);
                case OR:
                    return Resolver.or(resolve(c.c1), c.v1.state);
                case XOR:
                    return Resolver.xor(resolve(c.c1), c.v1.state);
            }
        } else if (c.v1 != null && c.v2 != null){
            //two var
            switch(c.operator) {
                case AND:
                    return Resolver.and(c.v1.state, c.v2.state);
                case OR:
                    return Resolver.or(c.v1.state, c.v2.state);
                case XOR:
                    return Resolver.xor(c.v1.state, c.v2.state);
            }
        } else if (c.v1 != null && c.v2 == null){
            //one var
            return c.v1.state;
        } else if (c.c1 != null && c.v1 == null){
            //one condition
            return resolve(c.c1);
        }
        throw new Exception("Should never be reached");
    }
}
