package ece659;

import java.util.Arrays;

/**
 *
 * @author jimmy
 */
public class IntQ {

    private final int[] state;
    private final Action[] actions;

    public IntQ(int[] state, Action[] actions) {
        this.state = state;
        this.actions = actions;
    }

    public int[] getState() {
        return state;
    }

    public Action[] getActions() {
        return actions;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntQ) {
            IntQ other = (IntQ) obj;
            return Arrays.equals(state, other.state) && Arrays.equals(actions, other.actions);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(state) ^ Arrays.hashCode(actions);
    }
}
