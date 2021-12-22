import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.Objects;

import sweeper.*;
import sweeper.Box;

public class JavaSweeper extends JFrame {

    private Game game;
    private JPanel panel;
    private JFrame jFrame = new JFrame();
    private String nameOfPlayer = "";
    private JLabel label;
    private ScoreFile file = new ScoreFile();


    private DecimalFormat textFormat = new DecimalFormat("00");// форматированный вывод
    private JavaTimer timer = new JavaTimer();

    private final int COLS = 9;
    private final int ROWS = 9;
    private final int IMAGE_SIZE = 50;
    private final int BOMBS = 1;


    public static void main(String[] args) {
        new JavaSweeper().setVisible(true);
    }
    private JavaSweeper (){
        game = new Game(COLS, ROWS, BOMBS);
        game.start();

        setImage();
        initLabel();
        initPanel();
        initFrame();
    }

    private void initLabel(){
        label = new JLabel("Welcome!");
        add(label, BorderLayout.SOUTH);
    }


    private void initPanel(){
        panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Coord coord: Ranges.getAllCords()){
                    g.drawImage((Image)game.getBox(coord).image,
                            coord.x * IMAGE_SIZE, coord.y * IMAGE_SIZE, this);
                }
            }
        };

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / IMAGE_SIZE;
                int y = e.getY() / IMAGE_SIZE;
                Coord coord = new Coord(x, y);
                timer.start();
                if (e.getButton() == MouseEvent.BUTTON1){
                    game.pressLeftButton(coord);
                    timer.printCurrentTimer();
                }
                if (e.getButton() == MouseEvent.BUTTON2){
                    game.start();
                    timer.reset();
                }
                if (e.getButton() == MouseEvent.BUTTON3){
                    game.pressRightButton(coord);
                }
                timer.endIfLoseOrWin(game.getState());
                label.setText(getMessage());
                panel.repaint();
                try {
                    sendDialogWindowIfEnd(jFrame);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        panel.setPreferredSize(new Dimension(Ranges.getSize().x * IMAGE_SIZE,
                                             Ranges.getSize().y * IMAGE_SIZE));
        add(panel);
    }

    private void sendDialogWindowIfEnd(JFrame jFrame) throws Exception{
        if (game.getState() == GameState.WINNER) {
            nameOfPlayer = JOptionPane.showInputDialog(jFrame,
                    "Write your name for scoreboard");
            if (!Objects.equals(nameOfPlayer, "") && nameOfPlayer != null)
                file.fileWrite(nameOfPlayer + " " + timer.getElapsedTime());
        }
    }

    private String getMessage() {
        return switch (game.getState()) {
            case PLAYED -> "Think twice!";
            case BOMBED -> "You lose(                           " +
                    "                                       " +
                    "                             Time: " + timer.getElapsedTime();
            case WINNER -> "Congratulations!!!                           " +
                    "                                       " +
                    "               Time: " + timer.getElapsedTime();
        };
    }


    private void initFrame(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Java Sweeper");
        setResizable(false);
        setVisible(true);
        setIconImage(getImage("icon"));
        pack();
        setLocationRelativeTo(null);
    }

    private void setImage(){
        for (Box box: Box.values())
            box.image = getImage(box.name());
    }


    private Image getImage(String name){
        String filename = "img/" + name.toLowerCase() + ".png";
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        return icon.getImage();
    }
}
