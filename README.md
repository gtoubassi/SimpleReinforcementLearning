# SimpleReinforcementLearning

![Cat and Mouse Gameboard](https://github.com/gtoubassi/SimpleReinforcementLearning/raw/master/assets/screenshot.png)

A simple demonstration of table based reinforcement learning using
[SARSA](https://en.wikipedia.org/wiki/State-Action-Reward-State-Action)
learning.  The goal is to train an agent to play a game of cat and mouse.
The game is played on a 5x5 grid.  The "cheese" is always in the upper
left of the playing board.  The mouse always starts in the lower right.
There is a barrier in the middle of the grid which the cat cannot see
through.  The cat will move around randomly until it can see the mouse
at which time it will move towards the mouse.

The player is the mouse.  If the cat catches (touches) the mouse the player
loses.  If the mouse touches the cheese the player wins.

A "win" garners a +1 reward, a "loss" a -1.  All other moves have a
configurable reward which should either be zero, or a small negative reward
to encourage faster game play.

### How to build

    % git clone https://github.com/gtoubassi/SimpleReinforcementLearning.git
    % cd SimpleReinforcementLearning/src
    % javac `find . -name '*.java' -print`

### How to run the command line version

    % java org.toubassi.rl.catmouse.commandline.CommandLineMain

### How to run the swing (gui) version

    % java org.toubassi.rl.catmouse.swing.SwingMain


You can run with a larger boardsize which is more difficult to learn
via the `--bigboard` command line argument.  This uses a 30x30 board
instead of the default 5x5.

You can also reward exploration via the `--explore` command line
argument.  Exploration is encouraged by adding a synthetic reward to
the environment reward which is based on the number of times the
reached state has been seen (1/sqrt(n)).  This reduces the learning
time significantly.  With `--bigboard` and no exploration it takes
~20,000 games before it is winning 95% of the time.  With ``--explore``
it takes ~7,000.
