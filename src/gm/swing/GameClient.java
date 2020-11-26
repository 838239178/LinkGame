package gm.swing;

import java.awt.*;

import javax.swing.*;

public class GameClient extends JFrame {
    private JPanel mainPanel;
    private JPanel switchPanel;
    private JMenuBar menuBar;
    private GamePanel gmPanel;
    private MessagePanel messagePanel;
    private EntryPanel entryPanel;
    private OverPanel overPanel;

    public GameClient() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        switchPanel = new JPanel(new CardLayout());
        mainPanel = new JPanel(new BorderLayout(2, 2));
        entryPanel = new EntryPanel();
        overPanel = new OverPanel();
        menuBar = new JMenuBar();
        gmPanel = new GamePanel(10);
        messagePanel = new MessagePanel(60);

        switchPanel.add(mainPanel, "play");
        switchPanel.add(entryPanel, "entry");
        switchPanel.add(overPanel, "over");

        //region testing
        JMenuItem about = new JMenuItem("about(B)");
        JMenu gameM = new JMenu("game(R)");
        gameM.add(about);
        menuBar.add(gameM);
        //endregion

        this.add(BorderLayout.CENTER, switchPanel);
        this.add(BorderLayout.SOUTH, new JPanel());
        this.add(BorderLayout.WEST, new JPanel());
        this.add(BorderLayout.NORTH, menuBar);

        mainPanel.add(BorderLayout.NORTH, messagePanel);
        mainPanel.add(BorderLayout.CENTER, gmPanel);

        this.pack();
        this.setSize(800,600);
    }

    public void gameStart() {
        messagePanel.startCountDown();
    }

    public static void main(String[] args) {
        new GameClient().gameStart();
    }
}
