package org.toubassi.rl.catmouse;

import java.util.Arrays;
import java.util.Random;

/**
 * Implements SARSA(lambda).  This performs a bit better then SARSA because
 * values are propagated back over the entire trajectory when encountered
 * (discounted by lambda) so information propagates more quickl through
 * state/action space.  It does this at some computational cost so in this
 * simple example with this naive/unoptimized implementation it is probably
 * slower (on balance) to train a winning agent.  Meaning it trains in
 * fewer episodes but burns more total CPU per episode.
 *
 * See https://webdocs.cs.ualberta.ca/~sutton/book/ebook/node77.html
 */
public class SarsaLambdaMousePlayer extends SarsaMousePlayer {
    private ActionValueTable E;
    private float lambda;

    public SarsaLambdaMousePlayer(CatMouseGame game) {
        this(game, .1f, .90f, .6f, .95f);
    }

    public SarsaLambdaMousePlayer(CatMouseGame game, float epsilon, float alpha, float lambda, float gamma) {
        super(game, epsilon, alpha, gamma);
        this.lambda = lambda;
        E = new ActionValueTable(Q.getNumStates(), Q.getNumActions());
    }

    @Override
    protected void updateQForSarsa(int lastState, int lastAction, float reward, int state, int action) {
        float qLast = Q.get(lastState, lastAction);
        float qThis = Q.get(state, action);
        float delta = reward + gamma * qThis - qLast;

        E.set(lastState, lastAction, E.get(lastState, lastAction) + 1f);

        for (int s = 0; s < Q.getNumStates(); s++) {
            for (int a = 0; a < Q.getNumActions(); a++) {
                Q.set(s, a, Q.get(s, a) + alpha * delta * E.get(s, a));
                E.set(s, a, E.get(s, a) * gamma * lambda);
            }
        }
    }

    public void endEpisode() {
        super.endEpisode();
        E.setAll(0f);
    }
}
