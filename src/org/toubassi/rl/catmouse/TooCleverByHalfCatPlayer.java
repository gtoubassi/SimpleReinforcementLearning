package org.toubassi.rl.catmouse;

import java.util.Random;

/**
 * The SimpleCatPlayer is so easy to beat I thought I'd make some
 * improvements.  First, the Simple random motion means the cat
 * doesn't explore very much.  So keep track of what positions the
 * cat visits and make them less likely to be returned too.  i.e.
 * if the cat move left have a slight prejudice against moving
 * right.  Also, make sure to avoid walls and never "Stay", so the
 * cat will always move.  Third, when the cat sees the mouse, move
 * toward that position even if the cat becomes invisible (it ducks
 * behind a wall).  Once it gets to the location if the mouse is
 * not visible then fall back on the (improved) random moves
 * described above.
 *
 * Turns out these "improvements" are "too clever by half".  I
 * think this is because the Cat's initial position next to the
 * cheese means its advantageous to just hang out by the cheese
 * vs exploring.  I imagine if the cheese was positioned randomly
 * this algorithm would be more useful.
 */
public class TooCleverByHalfCatPlayer extends Player {
    private Random random = new Random(123456L);
    private float visits[][];
    private Point lastSightedMousePosition;

    public TooCleverByHalfCatPlayer() {
        visits = new float[5][5];
        endEpisode();
    }

    public void endEpisode() {
        for (int i = 0; i < visits.length; i++) {
            for (int j = 0; j < visits[i].length; j++) {
                visits[i][j] = 1f;
            }
        }
    }

    private void updateVisits(Point position) {
        // Decay visits to a min of 1.0 so states we haven't seen
        // in awhile become more and more attractive
        for (int i = 0; i < visits.length; i++) {
            for (int j = 0; j < visits[i].length; j++) {
                visits[i][j] = Math.max(1f, visits[i][j] * .9f);
            }
        }

        // Increase the current position to reflect that we've been here,
        // and make it less attractive
        visits[position.x][position.y] *= 1.5f;
    }

    public Player.Move makeMove(CatMouseGame game, float reward) {
        Point cat = game.getCatPosition();
        Point mouse = game.getMousePosition();

        updateVisits(cat);

        if (canCatSeeMouse(game, cat, mouse)) {
            if (cat.equals(mouse)) {
                return Move.Stay;
            }
            lastSightedMousePosition = mouse.clone();
        }
        else {
            if (cat.equals(lastSightedMousePosition)) {
                lastSightedMousePosition = null;
            }
        }

        if (lastSightedMousePosition != null) {
            int deltaX = lastSightedMousePosition.x - cat.x;
            int deltaY = lastSightedMousePosition.y - cat.y;

            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                return deltaX > 0 ? Move.Right : Move.Left;
            }
            else {
                return deltaY > 0 ? Move.Down : Move.Up;
            }
        }
        else {
            // Random path, with preference for unexplored positions, and avoiding
            // the walls.  And never "Stay"

            float[] moveProbabilities = new float[4];
            Point p = new Point(0, 0);

            // Up
            p.x = cat.x;
            p.y = cat.y - 1;
            moveProbabilities[Move.Up.ordinal()] = game.isPointOnWall(p) ? 0f : 1 / visits[p.x][p.y];

            // Down
            p.x = cat.x;
            p.y = cat.y + 1;
            moveProbabilities[Move.Down.ordinal()] = game.isPointOnWall(p) ? 0f : 1 / visits[p.x][p.y];

            // Left
            p.x = cat.x - 1;
            p.y = cat.y;
            moveProbabilities[Move.Left.ordinal()] = game.isPointOnWall(p) ? 0f : 1 / visits[p.x][p.y];

            // Right
            p.x = cat.x + 1;
            p.y = cat.y;
            moveProbabilities[Move.Right.ordinal()] = game.isPointOnWall(p) ? 0f : 1 / visits[p.x][p.y];

            float sum = 0f;
            for (int i = 0; i < moveProbabilities.length; i++) {
                sum += moveProbabilities[i];
            }
            for (int i = 0; i < moveProbabilities.length; i++) {
                moveProbabilities[i] /= sum;
            }

            float diceRoll = random.nextFloat();

            float cumulativeProbability = 0f;
            for (int i = 0; i < moveProbabilities.length; i++) {
                cumulativeProbability += moveProbabilities[i];
                if (cumulativeProbability > diceRoll) {
                    return moves[i];
                }
            }

            // This shouldn't happen
            return Move.Stay;
        }
    }

    private boolean canCatSeeMouse(CatMouseGame game, Point cat, Point mouse) {
        Point p = new Point(cat.x, cat.y);

        while (!p.equals(mouse) && !game.isPointOnWall(p)) {
            int deltaX = mouse.x - p.x;
            int deltaY = mouse.y - p.y;

            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                p.x += deltaX / Math.abs(deltaX);
            }
            else {
                p.y += deltaY / Math.abs(deltaY);
            }
        }

        return p.equals(mouse);
    }
}
