package edu.touro.cs.mcon364;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Properties;


public class Pong extends JFrame implements Serializable {
    public static void main(String[] args) {
        new Pong();
    }

    private static int score = 0;
    private final JLabel score1 = new JLabel();
    private final JLabel score2 = new JLabel();
    private final JLabel score3 = new JLabel();
    private final JLabel scoreboard = new JLabel();
    private final JLabel status = new JLabel("PING PONG");


    public Pong() {
        setSize(700, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.add(new GamePanel(), BorderLayout.CENTER);

        JPanel scoreline = new JPanel();
        scoreline.setBackground(new Color(160, 32, 240));
        scoreboard.setForeground(new Color(25, 255, 0));
        scoreboard.setFont(new Font("Monospace", Font.BOLD, 20));
        scoreboard.setText("Score: ");
        scoreline.add(scoreboard);


        JPanel statusbar = new JPanel();
        statusbar.setBackground(new Color(160, 32, 240));
        status.setForeground(new Color(25, 255, 0));

        status.setFont(new Font("Monospace", Font.BOLD, 20));
        score1.setFont(new Font("Monospace", Font.BOLD, 20));
        score2.setFont(new Font("Monospace", Font.BOLD, 20));
        score3.setFont(new Font("Monospace", Font.BOLD, 20));
        score1.setForeground(new Color(25, 255, 0));
        score2.setForeground(new Color(25, 255, 0));
        score3.setForeground(new Color(25, 255, 0));
        statusbar.add(status);
        statusbar.add(score1);
        statusbar.add(score2);
        statusbar.add(score3);


        this.add(statusbar, BorderLayout.SOUTH);
        this.add(scoreline, BorderLayout.NORTH);
        setResizable(false);
        setVisible(true);
    }

    public void setScore(int score) {
        scoreboard.setText("Score: " + score);
    }


    private class GamePanel extends JPanel {
        private Point delta = new Point(3, 3);
        private Point paddle = new Point(20, 300);
        private final int PADDLE_WIDTH = 20;
        private final int PADDLE_HEIGHT = 80;
        private Point ball = new Point(100, 200);
        private edu.touro.cs.mcon364.PriorityQueue<scoreAndNames> scores_queue = new edu.touro.cs.mcon364.PriorityQueue<>(new scoreAndNames.scorecomparator());
        private final Properties prop = new Properties();
        private final int BALL_SIZE = 25;

        GamePanel() {
            this.setBorder(BorderFactory.createLineBorder(Color.yellow, 20));
            this.setBackground(Color.black);

                try {
                    prop.load(new FileInputStream("scores.ini"));
                    for (String names : prop.stringPropertyNames()) {
                        int number = Integer.parseInt(prop.getProperty(names));
                        scoreAndNames player_score = new scoreAndNames(names, number);
                        scores_queue.add(player_score);
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            Timer ballTimer = new Timer(10, // can't count on the timer to be accurate
                    e -> {
                        ball.translate(delta.x, delta.y);
                        // bounds check
                        if (ball.y <= 17 || ball.y >= 545) // at top or bottom
                        {
                            delta.y = -delta.y;
                        }
                        if (ball.x >= 640) {
                            delta.x = -delta.x;
                        }
                        if (ball.x <= 13) {
                            if (scores_queue.size() >= 3) {
                                if (score > scores_queue.get(2).getValue()) {
                                    String name = JOptionPane.showInputDialog("Game Over! : " + score + "\nPlease enter your initials");
                                    scoreAndNames player_score = new scoreAndNames(name, score);
                                    scores_queue.add(player_score);
                                }
                            }
                            if (scores_queue.size() < 3) {
                                String name = JOptionPane.showInputDialog("Game Over! : " + score + "\nPlease enter your initials");
                                scoreAndNames player_score = new scoreAndNames(name, score);
                                scores_queue.add(player_score);
                            }
                            int a = JOptionPane.showConfirmDialog(this, "New Game?", "", JOptionPane.YES_NO_OPTION);
                            if (a == JOptionPane.YES_OPTION) {
                                if (scores_queue.size() >= 3) {
                                    status.setText("High Scores: ");
                                    score1.setText(scores_queue.get(0).toString());
                                    score2.setText(scores_queue.get(1).toString());
                                    score3.setText(scores_queue.get(2).toString());
                                }
                                gamereset();
                            } else {
                                if (scores_queue.size() >= 3) {
                                    prop.put(scores_queue.get(0).name, scores_queue.get(0).value + "");
                                    prop.put(scores_queue.get(1).name, scores_queue.get(1).value + "");
                                    prop.put(scores_queue.get(2).name, scores_queue.get(2).value + "");
                                }
                                try {
                                    prop.store(new FileOutputStream("scores.ini"), "");
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                System.exit(0);
                            }
                        }

                        if (ball.x >= paddle.x && ball.x <= paddle.x + PADDLE_WIDTH) {
                            if (ball.y >= paddle.y && ball.y <= paddle.y + PADDLE_HEIGHT) {
                                score += 1;
                                setScore(score);
                                delta.x = -delta.x;
                            }
                        }
                        repaint();
                    });
            ballTimer.start();


            addMouseWheelListener(e -> {
                paddle.y += -e.getPreciseWheelRotation() * 5;
                repaint();
            });
        }

        private void gamereset() {
            score = 0;
            scoreboard.setText("Score: " + score);
            ball.x = 350;
            ball.y = 350;
            paddle.y = 300;
            paddle.x = 20;
            delta.x = -delta.x;

        }

        @Override
        public void paint(Graphics g) {
            super.paint(g); // call to super to do what JPANEL does and then we will do what we do
            g.setColor(Color.white);
            g.fillOval(ball.x, ball.y, BALL_SIZE, BALL_SIZE); // specify x,y which specifies bounding box and circle or oval is drawn on edges. Never touches x,y coordinate

            g.fillRect(paddle.x, paddle.y, PADDLE_WIDTH, PADDLE_HEIGHT);


        }

    }

    static class scoreAndNames {
        private final String name;
        private final int value;

        public scoreAndNames(String k, int v) {
            this.name = k;
            this.value = v;
        }


        public int getValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return name + ": " + value;

        }

        public static class scorecomparator implements Comparator<scoreAndNames> {

            @Override
            public int compare(scoreAndNames o1, scoreAndNames o2) {
                return Integer.compare(o2.value, o1.value);
            }
        }

    }


}
