# SimpleReinforcementLearning

A simple demonstration of reinforcement learning using
[SARSA](https://en.wikipedia.org/wiki/State-Action-Reward-State-Action)
learning.  The goal is to train an agent to play a game of cat and mouse.
The game is played on a 5x5 grid.  The cheese is in the upper left ('*').  
The cat starts near the cheese ('c').  The mouse starts in the lower right 
('m').  There is a barrier in the middle of the grid ('XXX') which the cat 
cannot see through.  The cat will move around randomly until it can see the 
mouse at which time it will move towards the mouse.

The player is the mouse.  If the cat catches (touches) the mouse the player
loses.  If the mouse touches the cheese the player wins.

A "win" garners a +1 reward, a "loss" a -1.  All other moves have no reward.

Run `org.toubassi.rl.catmouse.CatMouseGame` to see training results and
the result of the game board at each time step for each episode.  It runs
10000 episodes and wins 94% of the games (closing with a ~1400 win streak).

