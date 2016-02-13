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

    private Point catPos;
    private Point mousePos;
    private Player catPlayer;
    private Player mousePlayer;
    private int mouseWins;
    private int catWins;
    private int mouseWinStreak;

    public CatMouseGame(Player catPlayer, Player mousePlayer) {
        catPos = new Point(1, 1);
        mousePos = new Point(4, 4);
        this.catPlayer = catPlayer;
        this.mousePlayer = mousePlayer;
    }

    public Point getMousePosition() {
        return mousePos;
    }

    public Point getCatPosition() {
        return catPos;
    }

    public boolean isPointOnWall(Point p) {
        return (p.y == 2 && p.x >= 1 && p.x <= 3);
    }

    public void runEpisode() {
        int step = 0;
        printGame();
        while (!isGameOver()) {
            if (step % 2 == 0) {
                processMove(mousePos, mousePlayer.makeMove(this, 0f));
            }
            else {
                processMove(catPos, catPlayer.makeMove(this, 0f));
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

        System.out.print("Episode " + episodeCount() + " ");
        if (didMouseWin) {
            System.out.print("Mouse wins! ");
        }
        else {
            System.out.print("Mouse loses :-( ");
        }
        System.out.println("Mouse wins " + (((float) mouseWins) / episodeCount() * 100) + "% of the time (win streak = " + mouseWinStreak + ")");

        endEpisode();
    }

    private void endEpisode() {
        catPos = new Point(1, 1);
        mousePos = new Point(4, 4);
        catPlayer.endEpisode();
        mousePlayer.endEpisode();
    }

    private int episodeCount() {
        return mouseWins + catWins;
    }

    private void processMove(Point pos, Player.Move move) {
        switch (move) {
            case Up:
                if (pos.y == 0 || (pos.y == 3 && pos.x >= 1 && pos.x <= 3)) {
                    // you are hitting a wall NOOP
                }
                else {
                    pos.y--;
                }
                break;
            case Down:
                if (pos.y == 4 || (pos.y == 1 && pos.x >= 1 && pos.x <= 3)) {
                    // you are hitting a wall NOOP
                }
                else {
                    pos.y++;
                }
                break;
            case Left:
                if (pos.x == 0 || (pos.y == 2 && pos.x == 4)) {
                    // you are hitting a wall NOOP
                }
                else {
                    pos.x--;
                }
                break;
            case Right:
                if (pos.x == 4 || (pos.y == 2 && pos.x == 0)) {
                    // you are hitting a wall NOOP
                }
                else {
                    pos.x++;
                }
                break;
        }
    }

    public boolean isGameOver() {
        return mousePos.equals(CheesePos) || catPos.equals(mousePos);
    }

    private void printGame() {
        System.out.println("-------");
        for (int row = 0; row < 5; row++) {
            StringBuilder builder = new StringBuilder("     ");
            System.out.print("|");
            if (row == 0) {
                builder.setCharAt(0, '*'); // Cheese
            }
            if (row == 2) {
                builder.setCharAt(1, 'X');
                builder.setCharAt(2, 'X');
                builder.setCharAt(3, 'X');
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
        System.out.println("-------");
    }

    public static void main(String[] args) {
        CatMouseGame game = new CatMouseGame(new SimpleCatPlayer(), new SarsaMousePlayer());

        for (int i = 0; i < 10000; i++) {
            game.runEpisode();
        }
    }
}
