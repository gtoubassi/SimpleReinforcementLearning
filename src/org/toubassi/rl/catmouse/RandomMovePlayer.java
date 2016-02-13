package org.toubassi.rl.catmouse;

import java.util.Random;

/**
 * A player that moves purely randomly.  It can be used for both
 * the cat or the mouse.
 */
public class RandomMovePlayer extends Player {
    private Random random = new Random(123L);

    public Move makeMove(CatMouseGame game, float reward) {
        return moves[random.nextInt(moves.length)];
    }
}
