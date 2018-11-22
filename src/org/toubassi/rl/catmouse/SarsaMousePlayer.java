package org.toubassi.rl.catmouse;

/**
 * Implements a naive SARSA(0) learning agent that uses
 * an epsilon-greedy policy (epsilon starts at .1 and
 * decays by .999 every episode).
 */
public class SarsaMousePlayer extends Player {

    protected ActionValueTable Q;
    protected int maxPlayerState;
    protected int[][] positionToPlayerState;
    protected int[] stateVisitCount;
    protected float epsilon;
    protected float alpha;
    protected float gamma;
    protected int lastState = -1;
    protected int lastAction;
    protected boolean wasLastActionRandom;
    protected boolean rewardExploration;

    public SarsaMousePlayer(CatMouseGame game) {
        this(game, false);
    }

    public SarsaMousePlayer(CatMouseGame game, boolean rewardExploration) {
        this(game, .1f, .90f, .95f, rewardExploration);
    }

    public SarsaMousePlayer(CatMouseGame game, float epsilon, float alpha, float gamma, boolean rewardExploration) {
        this.epsilon = epsilon;
        this.alpha = alpha;
        this.gamma = gamma;
        this.rewardExploration = rewardExploration;

        // Calculate how many unique states the player can be in.
        // This is basically width * height - # of interior walls.
        // At the same time find a mapping of a (legal) position
        // to a state.
        maxPlayerState = 0;
        positionToPlayerState = new int[game.getWidth()][game.getHeight()];
        for (int y = 0; y < game.getHeight(); y++) {
            for (int x = 0; x < game.getWidth(); x++) {
                if (game.isPointOnWall(x, y)) {
                    positionToPlayerState[x][y] = -1;
                }
                else {
                    positionToPlayerState[x][y] = maxPlayerState++;
                }
            }
        }

        // Overall game state is conceptually two dimensions (which we
        // linearize) one dimension for each player which ranges
        // [0, maxPlayerState).
        this.Q = new ActionValueTable(maxPlayerState * maxPlayerState, moves.length);
        this.stateVisitCount = new int[maxPlayerState * maxPlayerState];
    }

    public Move makeMove(CatMouseGame game, float reward) {
        int state = stateForGame(game);
        int action = pickActionEpsilonGreedy(state);
        Move move = moves[action];

        if (rewardExploration) {
            stateVisitCount[state]++;
            reward += 1.0 / Math.sqrt(stateVisitCount[state]);
        }

        if (lastState != -1) {
            updateQForSarsa(lastState, lastAction, reward, state, action);
        }
        lastState = state;
        lastAction = action;
        return move;
    }

    protected void updateQForSarsa(int lastState, int lastAction, float reward, int state, int action) {
        float qLast = Q.get(lastState, lastAction);
        float qThis = Q.get(state, action);
        Q.set(lastState, lastAction, qLast + alpha * (reward + gamma * qThis - qLast));
    }

    public void endEpisode() {
        epsilon *= .999;
        lastState = -1;
        lastAction = 0;
    }

    private int pickActionEpsilonGreedy(int state) {
        if (random.nextFloat() > (1 - epsilon)) {
            wasLastActionRandom = true;
            return random.nextInt(moves.length);
        }

        wasLastActionRandom = false;

        int maxAction = 0;
        float maxValue = Q.get(state, 0);
        for (int i = 1; i < moves.length; i++) {
            if (Q.get(state, i) > maxValue) {
                maxValue = Q.get(state, i);
                maxAction = i;
            }
        }
        return maxAction;
    }

    private int stateForGame(CatMouseGame game) {
        Point catPos = game.getCatPosition();
        int catState = positionToPlayerState[catPos.x][catPos.y];
        Point mousePos = game.getMousePosition();
        int mouseState = positionToPlayerState[mousePos.x][mousePos.y];

        return catState * maxPlayerState + mouseState;
    }
}
