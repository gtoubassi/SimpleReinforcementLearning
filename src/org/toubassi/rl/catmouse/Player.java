package org.toubassi.rl.catmouse;

/**
 * Base class for all Cat or Mouse player agents.
 */
public abstract class Player {
    public enum Move {Up, Down, Left, Right, Stay};
    public static Move[] moves = Move.values();

    public abstract Move makeMove(CatMouseGame game, float reward);

    public void endEpisode() {}
}
