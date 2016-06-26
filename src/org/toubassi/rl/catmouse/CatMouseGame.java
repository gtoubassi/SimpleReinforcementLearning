package org.toubassi.rl.catmouse;

/**
 * A game of cat and mouse played on a 5x5 grid. The cheese is in the
 * upper left ('*').  The cat starts near the cheese ('c').  The mouse
 * starts in the lower right ('m').  There is a barrier in the middle
 * of the grid ('XXX') which the cat cannot see through.  The cat will
 * move around randomly until it can see the mouse at which time it
 * will move towards the mouse.
 *
 * The player is the mouse.  If the cat catches (touches) the mouse
 * the player loses.  If the mouse touches the cheese the player wins.
 *
 * A "win" garners a +1 reward, a "loss" a -1.  All other moves have
 * no reward.
 *
 *   -----
 *  |*    |
 *  | c   |
 *  | XXX |
 *  |     |
 *  |    m|
 *  -------
 */
public class CatMouseGame {
    private static Point CheesePos = new Point(0, 0);

    private int step;
    private int width;
    private int height;
    private float timeStepReward; // Set to -something like -.05 to encourage faster games
    private Point catPos;
    private Point mousePos;
    private Player catPlayer;
    private Player mousePlayer;
    private boolean verbose;

    public CatMouseGame(int width, int height, float timeStepReward, boolean verbose) {
        this.width = width;
        this.height = height;
        catPos = new Point(1, 1);
        mousePos = new Point(width - 1, height - 1);
        this.timeStepReward = timeStepReward;
        this.verbose = verbose;
    }

    public void setMousePlayer(Player player) {
        mousePlayer = player;
    }

    public void setCatPlayer(Player player) {
        catPlayer = player;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Point getMousePosition() {
        return mousePos;
    }

    public Point getCatPosition() {
        return catPos;
    }

    public boolean isPointOnWall(Point p) {
        return isPointOnWall(p.x, p.y);
    }

    public boolean isPointOnWall(int x, int y) {
        // First check exterior walls
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return true;
        }

        // Check the center barrier
        if (y == height / 2) {
            if (x >= (int)(.2 * width) && x < (int)(.8 * width)) {
                return true;
            }
        }

        return false;
    }


    public void step() {
        if (isGameOver()) {
            throw new RuntimeException("Cannot call step when game is over");
        }
        if (step > 1e6) {
            throw new RuntimeException("Too many steps for this episode: " + step);
        }

        if (step % 2 == 0) {
            processMove(mousePos, mousePlayer.makeMove(this, timeStepReward));
        }
        else {
            processMove(catPos, catPlayer.makeMove(this, timeStepReward));
        }
        if (mousePos.equals(catPos)) {
            mousePlayer.makeMove(this, -1f);
        }
        else if (mousePos.equals(CheesePos)){
            mousePlayer.makeMove(this, 1f);
        }
        step++;
    }

    public boolean didMouseWin() {
        if (!isGameOver()) {
            throw new RuntimeException("Can only assess winner if isGameOver");
        }
        return mousePos.equals(CheesePos) ? true : false;
    }

    public void resetEpisode() {
        step = 0;
        catPos = new Point(1, 1);
        mousePos = new Point(width - 1, height - 1);
        catPlayer.endEpisode();
        mousePlayer.endEpisode();
    }

    private void processMove(Point pos, Player.Move move) {
        switch (move) {
            case Up:
                if (!isPointOnWall(pos.x, pos.y - 1)) {
                    pos.y--;
                }
                break;
            case Down:
                if (!isPointOnWall(pos.x, pos.y + 1)) {
                    pos.y++;
                }
                break;
            case Left:
                if (!isPointOnWall(pos.x - 1, pos.y)) {
                    pos.x--;
                }
                break;
            case Right:
                if (!isPointOnWall(pos.x + 1, pos.y)) {
                    pos.x++;
                }
                break;
        }
    }

    public boolean isGameOver() {
        return mousePos.equals(CheesePos) || catPos.equals(mousePos);
    }

    private void printGame() {
        if (!verbose) {
            return;
        }
        for (int col = 0; col < width + 2; col++) {
            System.out.print('-');
        }
        System.out.println();
        for (int row = 0; row < height; row++) {
            StringBuilder builder = new StringBuilder();

            System.out.print("|");

            for (int col = 0; col < width; col++) {
                builder.append(' ');
                if (isPointOnWall(col, row)) {
                    builder.setCharAt(col, 'X');
                }
            }

            if (row == 0) {
                builder.setCharAt(0, '*'); // Cheese
            }
            if (row == mousePos.y) {
                builder.setCharAt(mousePos.x, 'm');
            }
            if (row == catPos.y) {
                builder.setCharAt(catPos.x, 'c');
            }
            System.out.print(builder.toString());
            System.out.println("|");
        }
        for (int col = 0; col < width + 2; col++) {
            System.out.print('-');
        }
        System.out.println();
    }
}
