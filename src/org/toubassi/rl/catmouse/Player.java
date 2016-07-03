package org.toubassi.rl.catmouse;

import java.util.Random;

/**
 * Base class for all Cat or Mouse player agents.
 */
public abstract class Player {
    protected static Random random = new Random(1234567L);

    public enum Move {Up, Down, Left, Right, Stay};
    public static Move[] moves = Move.values();

    public abstract Move makeMove(CatMouseGame game, float reward);

    public void endEpisode() {}
}
