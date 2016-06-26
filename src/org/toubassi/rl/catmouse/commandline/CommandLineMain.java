package org.toubassi.rl.catmouse.commandline;

import org.toubassi.rl.catmouse.CatMouseGame;
import org.toubassi.rl.catmouse.SarsaMousePlayer;
import org.toubassi.rl.catmouse.SimpleCatPlayer;

/**
 * Created by gtoubassi on 6/20/16.
 */
public class CommandLineMain {

    public static boolean runEpisode(CatMouseGame game) {

        while (!game.isGameOver()) {
            game.step();
        }

        boolean mouseWon = game.didMouseWin();
        game.resetEpisode();
        return mouseWon;
    }

    public static int runBatch(CatMouseGame game, int numEpisodes) {
        int mouseWins = 0;

        for (int i = 0; i < numEpisodes; i++) {
            if (runEpisode(game)) {
                mouseWins++;
            }
        }
        return mouseWins;
    }

    public static void main(String[] args) {
        CatMouseGame game = new CatMouseGame(5, 5, -.01f, false);
        game.setCatPlayer(new SimpleCatPlayer());
        game.setMousePlayer(new SarsaMousePlayer(game));

        int numEpisodesPerBatch = 100;
        for (int i = 0; i < 1000; i++) {
            int mouseWins = runBatch(game, numEpisodesPerBatch);
            System.out.println("Batch " + (i + 1) + " Mouse wins " + (mouseWins * 100 / numEpisodesPerBatch) + "% of the time");
        }
    }
}
