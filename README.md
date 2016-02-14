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

A "win" garners a +1 reward, a "loss" a -1.  All other moves have a configurable reward which should either be zero, or a small negative reward to encourage faster game play.

Run `org.toubassi.rl.catmouse.CatMouseGame` to see training results.  Specifying
verbose=true in the CatMouseGame constructor will show you the state of the game
board at each time step (lots of output!).  You can also customize the size of the
game board, but beware state representations for game boards more then about 50x50
consume a lot of memory (since our Q(s,a) vector is a naive lookup table).

Below is a sample run where the mouse manages to avoid the cat by going around the barrier the long way to get to the cheese.

    -------
    |*    |
    | c   |
    | XXX |
    |     |
    |    m|
    -------
    -------
    |*    |
    | c   |
    | XXX |
    |     |
    |   m |
    -------
    -------
    |*    |
    |c    |
    | XXX |
    |     |
    |   m |
    -------
    -------
    |*    |
    |c    |
    | XXX |
    |   m |
    |     |
    -------
    -------
    |*    |
    |c    |
    | XXX |
    |   m |
    |     |
    -------
    -------
    |*    |
    |c    |
    | XXX |
    |  m  |
    |     |
    -------
    -------
    |*    |
    |c    |
    | XXX |
    |  m  |
    |     |
    -------
    -------
    |*    |
    |c    |
    | XXX |
    |  m  |
    |     |
    -------
    -------
    |*    |
    | c   |
    | XXX |
    |  m  |
    |     |
    -------
    -------
    |*    |
    | c   |
    | XXX |
    | m   |
    |     |
    -------
    -------
    |*    |
    | c   |
    | XXX |
    | m   |
    |     |
    -------
    -------
    |*    |
    | c   |
    | XXX |
    | m   |
    |     |
    -------
    -------
    |*    |
    |c    |
    | XXX |
    | m   |
    |     |
    -------
    -------
    |*    |
    |c    |
    | XXX |
    | m   |
    |     |
    -------
    -------
    |*    |
    |     |
    |cXXX |
    | m   |
    |     |
    -------
    -------
    |*    |
    |     |
    |cXXX |
    |  m  |
    |     |
    -------
    -------
    |*    |
    |     |
    |cXXX |
    |  m  |
    |     |
    -------
    -------
    |*    |
    |     |
    |cXXX |
    |   m |
    |     |
    -------
    -------
    |*    |
    |     |
    | XXX |
    |c  m |
    |     |
    -------
    -------
    |*    |
    |     |
    | XXX |
    |c    |
    |   m |
    -------
    -------
    |*    |
    |     |
    | XXX |
    | c   |
    |   m |
    -------
    -------
    |*    |
    |     |
    | XXX |
    | c m |
    |     |
    -------
    -------
    |*    |
    |     |
    | XXX |
    |  cm |
    |     |
    -------
    -------
    |*    |
    |     |
    | XXX |
    |  c m|
    |     |
    -------
    -------
    |*    |
    |     |
    | XXX |
    |   cm|
    |     |
    -------
    -------
    |*    |
    |     |
    | XXXm|
    |   c |
    |     |
    -------
    -------
    |*    |
    |     |
    | XXXm|
    |     |
    |   c |
    -------
    -------
    |*    |
    |     |
    | XXXm|
    |     |
    |   c |
    -------
    -------
    |*    |
    |     |
    | XXXm|
    |     |
    |   c |
    -------
    -------
    |*    |
    |     |
    | XXXm|
    |     |
    |   c |
    -------
    -------
    |*    |
    |     |
    | XXXm|
    |   c |
    |     |
    -------
    -------
    |*    |
    |    m|
    | XXX |
    |   c |
    |     |
    -------
    -------
    |*    |
    |    m|
    | XXX |
    |    c|
    |     |
    -------
    -------
    |*    |
    |   m |
    | XXX |
    |    c|
    |     |
    -------
    -------
    |*    |
    |   m |
    | XXXc|
    |     |
    |     |
    -------
    -------
    |*    |
    |   m |
    | XXXc|
    |     |
    |     |
    -------
    -------
    |*    |
    |   mc|
    | XXX |
    |     |
    |     |
    -------
    -------
    |*    |
    |  m c|
    | XXX |
    |     |
    |     |
    -------
    -------
    |*    |
    |  mc |
    | XXX |
    |     |
    |     |
    -------
    -------
    |*    |
    | m c |
    | XXX |
    |     |
    |     |
    -------
    -------
    |*    |
    | mc  |
    | XXX |
    |     |
    |     |
    -------
    -------
    |*m   |
    |  c  |
    | XXX |
    |     |
    |     |
    -------
    -------
    |*mc  |
    |     |
    | XXX |
    |     |
    |     |
    -------
    -------
    |m c  |
    |     |
    | XXX |
    |     |
    |     |
    -------
    Mouse wins!