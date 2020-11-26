package gm.swing;

import java.awt.*;

import javax.swing.*;

public class GameClient extends JFrame {
    private JPanel holder;
    private GamePanel gmPanel;
    private MessagePanel noticePanel;
    private JMenuBar menuBar;
//	private JMenu menu1, menu2, menu3;

    public GameClient() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(800, 600);
        this.setVisible(true);

        holder = new JPanel(new BorderLayout(2, 2));
        menuBar = new JMenuBar();
        gmPanel = new GamePanel(new GridLayout(1,2,25,5));
        noticePanel = new MessagePanel(60);

        //region testing
        JMenuItem about = new JMenuItem("about(B)");
        JMenu gameM = new JMenu("game(R)");
        gameM.add(about);
        menuBar.add(gameM);

        Image img = new ImageIcon("img//test.png").getImage();

        System.out.println(img);

        Block block1 = new Block(0, img);
        Block block2 = new Block(0, img);

        gmPanel.add(block1);
        gmPanel.add(block2);
        //endregion

        this.add(BorderLayout.CENTER, holder);
        this.add(BorderLayout.SOUTH, new JPanel());
        this.add(BorderLayout.WEST, new JPanel());
        this.add(BorderLayout.NORTH, menuBar);

        holder.add(BorderLayout.NORTH, noticePanel);
        holder.add(BorderLayout.CENTER, gmPanel);

        //this.pack();
    }

    public void gameStart() {
        noticePanel.startCountDown();
    }

    public static void main(String[] args) {
        new GameClient().gameStart();
    }
}
