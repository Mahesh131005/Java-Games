import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

class Wumpusgame {
    public static void main(String args[]) {
        Wumpus w = new Wumpus();
    }
}

class Wumpus extends JPanel implements KeyListener {
    JFrame f = new JFrame();
    JLabel[] l = new JLabel[16];
    JLabel msg1, msg2, msg3;
    int pos = 0;
    int p = 0,gold=0,wolf=0;
    int score= 0;
    int[] pits;
    String direction = "RIGHT"; 

    Wumpus() {
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(400, 400);
        msg1 = new JLabel("Welcome to The Game", SwingConstants.CENTER);
        msg2 = new JLabel("For wolf", SwingConstants.CENTER);
        msg3 = new JLabel("For glitter", SwingConstants.CENTER);
        msg1.setFont(new Font("Arial", Font.BOLD, 16));

        f.setLayout(new BorderLayout());
        f.add(msg1, BorderLayout.NORTH);
        f.add(msg2, BorderLayout.SOUTH);
        f.add(msg3, BorderLayout.WEST);

        this.setLayout(new GridLayout(4, 4));
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

        for (int i = 0; i < 16; i++) {
            l[i] = new JLabel(i + "", SwingConstants.CENTER);
            l[i].setBorder(border);
            this.add(l[i]);
        }
        l[pos].setText("You are here");
        
        this.setFocusable(true);
        this.addKeyListener(this);

        f.add(this, BorderLayout.CENTER);
        f.setVisible(true);

        gamestart();
    }

    void gamestart() {
        Scanner in = new Scanner(System.in);

        System.out.println("Enter the number of pits:");
        p = in.nextInt();
        pits = new int[p];
        
        for (int i = 0; i < p; i++) {
            System.out.println("Enter the position of pit " );
            pits[i] = in.nextInt();
        }
        
        System.out.println("Enter the position of the wolf:");
        wolf = in.nextInt();
        
        System.out.println("Enter the position of the gold:");
        gold = in.nextInt();

        in.close();
    }

    void checknear() {
        boolean nearPit = false;
        boolean nearWolf = false;

        for (int pi : pits) {
            if (pos == pi - 4 || pos == pi + 4 || pos - 1 == pi || pos + 1 == pi) {
                nearPit = true;
                break;
            }
        }
        msg1.setText(nearPit ? "Contains Breeze" : "Welcome");

        if (pos == wolf - 4 || pos == wolf + 4 || pos - 1 == wolf || pos + 1 == wolf) {
            nearWolf = true;
        }
        msg2.setText(nearWolf ? "Contains smell" : "For wolf");

        if (pos == gold) {
            msg3.setText("This room is Glittering");
            gamecomplete();
        } else {
            msg3.setText("For glitter");
        }

        for (int pi : pits) {
            if (pos == pi) {
                score -= 1000;
                msg1.setText("Fell into a pit! Score: " + score);
                break;
            }
        }

        if (pos == wolf) {
            score -= 1000;
            msg1.setText("Caught by the wolf! Score: " + score);
        }
    }

    void gamecomplete() {
        score += 1000;
        msg1.setText("You found the gold! Your score is " + score);
    }

    void shoot() {
        int targetPos = -1;
        switch (direction) {
            case "UP": targetPos = pos - 4; break;
            case "DOWN": targetPos = pos + 4; break;
            case "LEFT": targetPos = pos - 1; break;
            case "RIGHT": targetPos = pos + 1; break;
        }

        if (targetPos == wolf) {
            score += 500;
            msg1.setText("Wolf shot! Your score is " + score);
            wolf = -1; // Removes the wolf from the grid
        } else {
            msg1.setText("Missed the shot! Score: " + score);
            score -= 10; // Penalty for missing
        }
    }

    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        switch (keycode) {
            case KeyEvent.VK_UP:
                if (pos - 4 >= 0) {
                    l[pos].setText(pos + "");
                    pos -= 4;
                    direction = "UP";
                    l[pos].setText("You are here");
                    checknear();
                    score--;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (pos + 4 < 16) {
                    l[pos].setText(pos + "");
                    pos += 4;
                    direction = "DOWN";
                    l[pos].setText("You are here");
                    checknear();
                    score--;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (pos % 4 != 0) {
                    l[pos].setText(pos + "");
                    pos -= 1;
                    direction = "LEFT";
                    l[pos].setText("You are here");
                    checknear();
                    score--;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (pos % 4 != 3) {
                    l[pos].setText(pos + "");
                    pos += 1;
                    direction = "RIGHT";
                    l[pos].setText("You are here");
                    checknear();
                    score--;
                }
                break;
            case KeyEvent.VK_SPACE: // Shoot
                shoot();
                break;
        }
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}
}
