package org.toubassi.rl.catmouse;

import java.util.Random;

/**
 * Implements a naive SARSA(0) learning agent that uses
 * an epsilon-greedy policy (epsilon starts at .1 and
 * decays by .999 every episode).
 */
public class SarsaMousePlayer extends Player {

    protected Random random = new Random(123456L);
    protected float[][] Q = new float[25*32+25][5];
    protected float epsilon;
    protected float alpha;
    protected float gamma;
    protected int lastState = -1;
    protected int lastAction;
    protected boolean wasLastActionRandom;

    public SarsaMousePlayer() {
        this(.1f, .90f, .95f);
    }

    public SarsaMousePlayer(float epsilon, float alpha, float gamma) {
        this.epsilon = epsilon;
        this.alpha = alpha;
        this.gamma = gamma;
    }

    public Move makeMove(CatMouseGame game, float reward) {
        int state = stateForGame(game);
        int action = pickActionEpsilonGreedy(state);
        Move move = moves[action];

        if (lastState != -1) {
            updateQForSarsa(lastState, lastAction, reward, state, action);
        }
        lastState = state;
        lastAction = action;
        return move;
    }

    protected void updateQForSarsa(int lastState, int lastAction, float reward, int state, int action) {
        float qLast = Q[lastState][lastAction];
        float qThis = Q[state][action];
        Q[lastState][lastAction] += alpha * (reward + gamma * qThis - qLast);
    }

    public void endEpisode() {
        epsilon *= .999;
        lastState = -1;
        lastAction = 0;
    }

    private int pickActionEpsilonGreedy(int state) {
        if (random.nextFloat() > (1 - epsilon)) {
            wasLastActionRandom = true;
            return random.nextInt(5);
        }

        wasLastActionRandom = false;

        int maxAction = 0;
        float maxValue = Q[state][0];
        for (int i = 1; i < 5; i++) {
            if (Q[state][i] > maxValue) {
                maxValue = Q[state][i];
                maxAction = i;
            }
        }
        return maxAction;
    }

    private int stateForGame(CatMouseGame game) {
        Point catPos = game.getCatPosition();
        int catState = catPos.y * 5 + catPos.x;
        Point mousePos = game.getMousePosition();
        int mouseState = mousePos.y * 5 + mousePos.x;

        // This is an odd/sparse encoding for convenience.
        // It implies there are 10 bits worth of states (2^10=1024)
        // But really there are only 22 positions the cat and mouse
        // can be in (25 - the 3 walls), so there are only 22*22 = 484
        // states.
        return (catState << 5) | mouseState;
    }

    public void printLastStateAction() {
        int mouseState = lastState & 31;
        int catState = lastState >> 5;
        int mouseX = mouseState % 5;
        int mouseY = mouseState / 5;
        int catX = catState % 5;
        int catY = catState / 5;
        System.out.println("Last: m=" + mouseX + "," + mouseY + "  c=" + catX + "," + catY + "  " + lastState + " " + moves[lastAction] + " " + wasLastActionRandom);
    }

}
