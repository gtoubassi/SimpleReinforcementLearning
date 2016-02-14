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

    private int width;
    private int height;
    private float timeStepReward; // Set to -something like -.05 to encourage faster games
    private Point catPos;
    private Point mousePos;
    private Player catPlayer;
    private Player mousePlayer;
    private int mouseWins;
    private int catWins;
    private int mouseWinStreak;
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

    public int runBatch(int numEpisodes) {
        mouseWins = catWins = 0;
        int numSteps = 0;
        for (int i = 0; i < numEpisodes; i++) {
            numSteps += runEpisode();
        }
        //System.out.println("Total steps in batch: " + numSteps);
        return mouseWins;
    }

    public int runEpisode() {
        int step = 0;
        printGame();
        while (!isGameOver()) {
            if (step % 2 == 0) {
                processMove(mousePos, mousePlayer.makeMove(this, timeStepReward));
            }
            else {
                processMove(catPos, catPlayer.makeMove(this, timeStepReward));
            }
            printGame();
            step++;
        }

        boolean didMouseWin;

        if (mousePos.equals(catPos)) {
            mousePlayer.makeMove(this, -1f);
            didMouseWin = false;
            catWins++;
            mouseWinStreak = 0;
        }
        else if (mousePos.equals(CheesePos)){
            mousePlayer.makeMove(this, 1f);
            didMouseWin = true;
            mouseWins++;
            mouseWinStreak++;
        }
        else {
            throw new RuntimeException("Unexpected termination");
        }

        if (verbose) {
            System.out.print("Episode " + episodeCount() + " ");
            if (didMouseWin) {
                System.out.print("Mouse wins! ");
            }
            else {
                System.out.print("Mouse loses :-( ");
            }
            System.out.println("Mouse wins " + (((float) mouseWins) / episodeCount() * 100) + "% of the time (win streak = " + mouseWinStreak + ") " + step);
        }

        endEpisode();
        return step;
    }

    private void endEpisode() {
        catPos = new Point(1, 1);
        mousePos = new Point(width - 1, height - 1);
        catPlayer.endEpisode();
        mousePlayer.endEpisode();
    }

    private int episodeCount() {
        return mouseWins + catWins;
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

    public static void main(String[] args) {
        CatMouseGame game = new CatMouseGame(5, 5, -.05f, false);
        game.setCatPlayer(new SimpleCatPlayer());
        game.setMousePlayer(new SarsaMousePlayer(game));

        int numEpisodesPerBatch = 100;
        for (int i = 0; i < 100; i++) {
            int mouseWins = game.runBatch(numEpisodesPerBatch);
            System.out.println("Batch " + (i + 1) + " Mouse wins " + (mouseWins * 100 / numEpisodesPerBatch) + "% of the time");
        }
    }
}
