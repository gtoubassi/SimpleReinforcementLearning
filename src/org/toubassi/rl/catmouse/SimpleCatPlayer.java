package org.toubassi.rl.catmouse;

/**
 * A simple cat player that moves randomly until it can "see"
 * the mouse, where "see" means there is an unobstructed line
 * from cat to mouse.  If so it will move towards the mouse.
 */
public class SimpleCatPlayer extends RandomMovePlayer {

    public Player.Move makeMove(CatMouseGame game, float reward) {
        Point cat = game.getCatPosition();
        Point mouse = game.getMousePosition();

        if (canCatSeeMouse(game, cat, mouse)) {
            int deltaX = mouse.x - cat.x;
            int deltaY = mouse.y - cat.y;

            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                return deltaX > 0 ? Move.Right : Move.Left;
            }
            else {
                return deltaY > 0 ? Move.Down : Move.Up;
            }
        }
        return super.makeMove(game, reward);
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
