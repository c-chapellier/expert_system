package resolver;
import model.*;

public class Resolver {

    public static State not(State state) {
        if (state == State.FALSE) {
            return State.TRUE;
        } else if (state == State.TRUE) {
            return State.FALSE;
        } else {
            return State.UNDEFINED;
        }
    }

    public static State and(State state1, State state2){
        if(state1 == State.TRUE && state2 == State.TRUE){
            return State.TRUE;
        } else if (state1 == State.FALSE || state2 == State.FALSE) {
            return State.FALSE;
        } else {
            return State.UNDEFINED;
        }
    }

    public static State or(State state1, State state2){
        if(state1 == State.TRUE || state2 == State.TRUE){
            return State.TRUE;
        } else if (state1 == State.FALSE && state2 == State.FALSE) {
            return State.FALSE;
        } else {
            return State.UNDEFINED;
        }
    }

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
}
