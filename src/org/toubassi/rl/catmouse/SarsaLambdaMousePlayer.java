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
    private float[][] E = new float[25*32+25][5];
    private float lambda;

    public SarsaLambdaMousePlayer() {
        this(.1f, .90f, .6f, .95f);
    }

    public SarsaLambdaMousePlayer(float epsilon, float alpha, float lambda, float gamma) {
        super(epsilon, alpha, gamma);
        this.lambda = lambda;
    }

    @Override
    protected void updateQForSarsa(int lastState, int lastAction, float reward, int state, int action) {
        float qLast = Q[lastState][lastAction];
        float qThis = Q[state][action];
        float delta = reward + gamma * qThis - qLast;

        E[lastState][lastAction] += 1f;

        for (int s = 0; s < Q.length; s++) {
            for (int a = 0; a < Q[s].length; a++) {
                Q[s][a] += alpha * delta * E[s][a];
                E[s][a] *= gamma * lambda;
            }
        }
    }

    public void endEpisode() {
        super.endEpisode();
        for (int i = 0; i < E.length; i++) {
            Arrays.fill(E[i], 0f);
        }
    }
}
