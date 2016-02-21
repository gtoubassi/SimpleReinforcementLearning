package org.toubassi.rl.catmouse;

import java.util.Arrays;

/**
 * Implements "Q"
 */
public class ActionValueTable {

    private float Q[];
    private int numStates;
    private int numActions;

    public ActionValueTable(int numStates, int numActions) {
        Q = new float[numStates * numActions];
        this.numStates = numStates;
        this.numActions = numActions;
    }

    public int getNumStates() {
        return numStates;
    }

    public int getNumActions() {
        return numActions;
    }

    public void setAll(float v) {
        Arrays.fill(Q, v);
    }

    public float get(int state, int action) {
        return Q[state * numActions + action];
    }

    public void set(int state, int action, float value) {
        Q[state * numActions + action] = value;
    }
}
