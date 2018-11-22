package org.toubassi.rl.catmouse;

/**
 * A player that moves purely randomly.  It can be used for both
 * the cat or the mouse.
 */
public class RandomMovePlayer extends Player {
    public Move makeMove(CatMouseGame game, float reward) {
        return moves[random.nextInt(moves.length)];
    }
}
