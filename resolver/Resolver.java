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

    public static State resolve(Condition c){
        if (c.c1 != null && c.c2 != null){
            //two conditions
            switch(c.operator) {
                case AND:
                    if(!c.not1 && !c.not2){
                        return Resolver.and(resolve(c.c1), resolve(c.c2));
                    } else if (c.not1 && !c.not2) {
                        return Resolver.and(not(resolve(c.c1)), resolve(c.c2));
                    } else if (!c.not1 && c.not2) {
                        return Resolver.and(resolve(c.c1), not(resolve(c.c2)));
                    } else {
                        return Resolver.and(not(resolve(c.c1)), not(resolve(c.c2)));
                    }
                case OR:
                    if(!c.not1 && !c.not2){
                        return Resolver.or(resolve(c.c1), resolve(c.c2));
                    } else if (c.not1 && !c.not2) {
                        return Resolver.or(not(resolve(c.c1)), resolve(c.c2));
                    } else if (!c.not1 && c.not2) {
                        return Resolver.or(resolve(c.c1), not(resolve(c.c2)));
                    } else {
                        return Resolver.or(not(resolve(c.c1)), not(resolve(c.c2)));
                    }
                case XOR:
                    if(!c.not1 && !c.not2){
                        return Resolver.xor(resolve(c.c1), resolve(c.c2));
                    } else if (c.not1 && !c.not2) {
                        return Resolver.xor(not(resolve(c.c1)), resolve(c.c2));
                    } else if (!c.not1 && c.not2) {
                        return Resolver.xor(resolve(c.c1), not(resolve(c.c2)));
                    } else {
                        return Resolver.xor(not(resolve(c.c1)), not(resolve(c.c2)));
                    }
            }
        } else if (c.c1 != null && c.v1 != null){
            //one condition and one var
            switch(c.operator) {
                case AND:
                    if(!c.not1 && !c.not2){
                        return Resolver.and(resolve(c.c1), c.v1.state);
                    } else if (c.not1 && !c.not2) {
                        return Resolver.and(not(resolve(c.c1)), c.v1.state);
                    } else if (!c.not1 && c.not2) {
                        return Resolver.and(resolve(c.c1), not(c.v1.state));
                    } else {
                        return Resolver.and(not(resolve(c.c1)), not(c.v1.state));
                    }
                case OR:
                    if(!c.not1 && !c.not2){
                        return Resolver.or(resolve(c.c1), c.v1.state);
                    } else if (c.not1 && !c.not2) {
                        return Resolver.or(not(resolve(c.c1)), c.v1.state);
                    } else if (!c.not1 && c.not2) {
                        return Resolver.or(resolve(c.c1), not(c.v1.state));
                    } else {
                        return Resolver.or(not(resolve(c.c1)), not(c.v1.state));
                    }
                case XOR:
                    if(!c.not1 && !c.not2){
                        return Resolver.xor(resolve(c.c1), c.v1.state);
                    } else if (c.not1 && !c.not2) {
                        return Resolver.xor(not(resolve(c.c1)), c.v1.state);
                    } else if (!c.not1 && c.not2) {
                        return Resolver.xor(resolve(c.c1), not(c.v1.state));
                    } else {
                        return Resolver.xor(not(resolve(c.c1)), not(c.v1.state));
                    }
            }
        } else if (c.v1 != null && c.v2 != null){
            //two var
            switch(c.operator) {
                case AND:
                    if(!c.not1 && !c.not2){
                        return Resolver.and(c.v1.state, c.v2.state);
                    } else if(c.not1 && !c.not2) {
                        return Resolver.and(not(c.v1.state), c.v2.state);
                    } else if (!c.not1 && c.not2) {
                        return Resolver.and(c.v1.state, not(c.v2.state));
                    } else {
                        return Resolver.and(not(c.v1.state), not(c.v2.state));
                    }
                case OR:
                    if(!c.not1 && !c.not2){
                        return Resolver.or(c.v1.state, c.v2.state);
                    } else if(c.not1 && !c.not2) {
                        return Resolver.or(not(c.v1.state), c.v2.state);
                    } else if (!c.not1 && c.not2) {
                        return Resolver.or(c.v1.state, not(c.v2.state));
                    } else {
                        return Resolver.or(not(c.v1.state), not(c.v2.state));
                    }
                case XOR:
                    if(!c.not1 && !c.not2){
                        return Resolver.xor(c.v1.state, c.v2.state);
                    } else if(c.not1 && !c.not2) {
                        return Resolver.xor(not(c.v1.state), c.v2.state);
                    } else if (!c.not1 && c.not2) {
                        return Resolver.xor(c.v1.state, not(c.v2.state));
                    } else {
                        return Resolver.xor(not(c.v1.state), not(c.v2.state));
                    }
            }
        } else if (c.v1 != null && c.v2 == null){
            //one var
            return c.not1 ? not(c.v1.state) : c.v1.state;
        } else if (c.c1 != null && c.v1 == null){
            //one condition
            return c.not1 ? not(resolve(c.c1)) : resolve(c.c1);
        }
        //Never reached
        return State.UNDEFINED;
    }
}
