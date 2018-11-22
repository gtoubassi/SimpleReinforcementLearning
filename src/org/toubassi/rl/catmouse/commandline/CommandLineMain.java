package org.toubassi.rl.catmouse.commandline;

import org.toubassi.rl.catmouse.CatMouseGame;
import org.toubassi.rl.catmouse.SarsaMousePlayer;
import org.toubassi.rl.catmouse.SimpleCatPlayer;

import java.util.Arrays;

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
        int boardSize = 5;
        boolean rewardExploration = Arrays.asList(args).contains("--explore");

        if (Arrays.asList(args).contains("--bigboard")) {
            boardSize = 30;
        }


        CatMouseGame game = new CatMouseGame(boardSize, boardSize, -.01f, false);
        game.setCatPlayer(new SimpleCatPlayer());
        game.setMousePlayer(new SarsaMousePlayer(game, rewardExploration));

        int numEpisodesPerBatch = 100;
        for (int i = 0; i < 1000; i++) {
            int mouseWins = runBatch(game, numEpisodesPerBatch);
            float percentage = (mouseWins * 100 / numEpisodesPerBatch);
            System.out.println("Batch " + (i + 1) + " Mouse wins " + (mouseWins * 100 / numEpisodesPerBatch) + "% of the time");
            if (percentage >= 95) {
                break;
            }
        }
    }
}
