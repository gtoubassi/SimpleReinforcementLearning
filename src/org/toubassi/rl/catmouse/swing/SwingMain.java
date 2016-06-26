package org.toubassi.rl.catmouse.swing;

import org.toubassi.rl.catmouse.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingMain implements ActionListener {

    private CatMouseGame game;
    private JLabel labels[][];
    private ImageIcon pillIcon;
    private ImageIcon catIcon;
    private ImageIcon mouseIcon;
    private ImageIcon emptyIcon;
    private ImageIcon wallIcon;
    private JTextArea textArea;
    private Timer timer;
    private JSlider speedSlider;

    public SwingMain() {
        game = new CatMouseGame(5, 5, -.05f, false);
        game.setCatPlayer(new SimpleCatPlayer());
        game.setMousePlayer(new SarsaMousePlayer(game));
        game.resetEpisode();
        labels = new JLabel[game.getWidth()][game.getHeight()];
    }

    public void createAndShowGUI() {

        pillIcon = new ImageIcon(SwingMain.class.getResource("/images/pill.png"));
        catIcon = new ImageIcon(SwingMain.class.getResource("/images/ghost.png"));
        mouseIcon = new ImageIcon(SwingMain.class.getResource("/images/pacman.png"));
        emptyIcon = new ImageIcon(SwingMain.class.getResource("/images/black.png"));
        wallIcon = new ImageIcon(SwingMain.class.getResource("/images/blue.png"));

        //Create and set up the window.
        JFrame frame = new JFrame("Simple Pac-Man");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBackground(Color.black);
        panel.setLayout(new GridLayout(game.getWidth(), game.getHeight()));

        for (int y = 0; y < game.getHeight(); y++) {
            for (int x = 0; x < game.getWidth(); x++) {
                JLabel label = new JLabel(game.isPointOnWall(x, y) ? wallIcon : emptyIcon);
                panel.add(label);
                labels[x][y] = label;
            }
        }
        org.toubassi.rl.catmouse.Point point = game.getCatPosition();
        labels[point.x][point.y].setIcon(catIcon);
        point = game.getMousePosition();
        labels[point.x][point.y].setIcon(mouseIcon);
        labels[0][0].setIcon(pillIcon);

        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));

        frame.getContentPane().add(panel);
        textArea = new JTextArea(25, 8);
        textArea.setFont(new Font("Verdana", Font.BOLD, 24));
        textArea.setEditable(false);
        frame.getContentPane().add(textArea);

        speedSlider = new JSlider(SwingConstants.VERTICAL, 1, 500, 250);
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                timer.stop();
                timer.setDelay(speedSlider.getValue());
                timer.start();
            }
        });
        frame.getContentPane().add(speedSlider);

        JToggleButton startStopButton = new JToggleButton("Run", false);
        startStopButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (((JToggleButton)(e.getSource())).isSelected()) {
                    timer.start();
                }
                else {
                    timer.stop();
                }
            }
        });
        frame.getContentPane().add(startStopButton);

        //Display the window.
        frame.pack();
        frame.setVisible(true);

        timer = new Timer(speedSlider.getValue(), this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        org.toubassi.rl.catmouse.Point catPosition = game.getCatPosition().clone();
        org.toubassi.rl.catmouse.Point mousePosition = game.getMousePosition().clone();

        if (game.isGameOver()) {
            textArea.insert(game.didMouseWin() ? "WIN!\n" : "lose\n", 0);
            game.resetEpisode();
        }

        game.step();

        labels[mousePosition.x][mousePosition.y].setIcon(mousePosition.x + mousePosition.y == 0 ? pillIcon : emptyIcon);
        labels[catPosition.x][catPosition.y].setIcon(catPosition.x + catPosition.y == 0 ? pillIcon : emptyIcon);
        catPosition = game.getCatPosition();
        mousePosition = game.getMousePosition();

        labels[mousePosition.x][mousePosition.y].setIcon(mouseIcon);
        labels[catPosition.x][catPosition.y].setIcon(catIcon);
    }

    public static void main(String[] args) {
        final SwingMain main = new SwingMain();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                main.createAndShowGUI();
            }
        });
    }
}
