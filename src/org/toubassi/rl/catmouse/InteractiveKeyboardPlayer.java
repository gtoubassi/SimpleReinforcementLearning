package org.toubassi.rl.catmouse;

import java.io.IOException;

/**
 * A player that takes input from the keyboard.  It can be used for both
 * the cat or the mouse.
 */
public class InteractiveKeyboardPlayer extends Player {

    public Player.Move makeMove(CatMouseGame game, float reward) {
        byte[] buf = new byte[4];

        if (game.isGameOver()) {
            // We get invoked one final time to give us the reward
            // which is not useful for this player.
            return Move.Stay;
        }

        try {
            while (true) {
                System.out.print("Enter a move (u=up, d=down, l=left, r=right, s=stay): ");
                System.in.read(buf);
                switch (buf[0]) {
                    case 'u':
                        return Move.Up;
                    case 'd':
                        return Move.Down;
                    case 'l':
                        return Move.Left;
                    case 'r':
                        return Move.Right;
                    case 's':
                        return Move.Stay;
                }
                System.out.println("Illegal character, try again");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
