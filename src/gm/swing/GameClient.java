package gm.swing;


import gm.game.Point;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public class GameClient extends JFrame {
    public final static int DIFF = 10;
    public final static int MED = 8;
    public final static int EASY = 6;

    private final int default_width = 780;
    private final int default_height = 750;

    private final JPanel mainPanel;
    private final JPanel switchPanel;
    private final JMenuBar menuBar;
    private final EntryPanel entryPanel;
    private final OverPanel overPanel;
    private final AboutDialog aboutDialog;
    private final JLabel backImage;

    private GamePanel gamePanel;
    private MessagePanel messagePanel;

    private int gameLevel;
    private String sceneName;

    private Sound bgm;

    public GameClient() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setTitle("连连看");

        //region ...initial panels
        aboutDialog = new AboutDialog(this);
        switchPanel = new JPanel(new CardLayout());
        mainPanel = new JPanel(new BorderLayout(2, 2));
        entryPanel = new EntryPanel();
        overPanel = new OverPanel();
        menuBar = new JMenuBar();

        entryPanel.addLevelChangeListener(e -> {
            gameLevel = e.getID();
            gameStart();
        });
        entryPanel.addClientExitListener(e -> {
            aboutDialog.dispose();
            dispose();
        });

        overPanel.addExitActonListener(e -> gameExit());
        overPanel.addRestartActonListener(e -> restartGame());
        //endregion

        //region ...initial menu
        {
            JMenu settingMenu = new JMenu("Setting (Alt+S)");
            JMenu gameMenu = new JMenu("Game (Alt+G)");
            JMenu resizeMenu = new JMenu("Resize");
            JMenuItem restart = new JMenuItem("Restart");
            JMenuItem refresh = new JMenuItem("Refresh");
            JMenuItem tips = new JMenuItem("Tips");
            JMenuItem quit = new JMenuItem("Quit");
            JMenuItem pause = new JMenuItem("Pause");

            gameMenu.setMnemonic(KeyEvent.VK_G);
            settingMenu.setMnemonic(KeyEvent.VK_S);

            quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
            quit.addActionListener(e -> {
                System.out.println("quit");
                if (sceneName.equals("play"))
                    gameExit();
            });

            resizeMenu.add("restore").addActionListener(e -> scaleSize(1));
            resizeMenu.add("x2").addActionListener(e -> scaleSize(2));
            resizeMenu.add("x3").addActionListener(e -> scaleSize(3));

            settingMenu.add("on/off bgm").addActionListener(e->{

            });
            settingMenu.add(resizeMenu);
            settingMenu.add("about").addActionListener(e -> aboutDialog.setVisible(true));

            restart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
            restart.addActionListener(e -> {
                if (sceneName.equals("play")) {
                    restartGame();
                }
            });

            refresh.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
            refresh.addActionListener(e -> {
                if (sceneName.equals("play")) {
                    gamePanel.refreshBlocks();
                    messagePanel.addRefreshCount(1);
                }
            });

            tips.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK));
            tips.addActionListener(e -> {
                if (sceneName.equals("play")) {
                    gamePanel.tipBlock();
                    messagePanel.addTipCount(1);
                }
            });

            pause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));
            pause.addActionListener(e -> gamePause());

            gameMenu.add(refresh);
            gameMenu.add(quit);
            gameMenu.add(tips);
            gameMenu.add(restart);
            gameMenu.add(pause);

            menuBar.add(gameMenu);
            menuBar.add(settingMenu);
        }
        //endregion

        //region ...add components
        switchPanel.add(mainPanel, "play");
        switchPanel.add(entryPanel, "entry");
        switchPanel.add(overPanel, "over");

        this.add(BorderLayout.CENTER, switchPanel);
        this.add(BorderLayout.SOUTH, new JPanel());
        this.add(BorderLayout.WEST, new JPanel());
        this.add(BorderLayout.EAST, new JPanel());
        this.add(BorderLayout.NORTH, menuBar);

        mainPanel.add(BorderLayout.WEST, new JPanel());
        mainPanel.add(BorderLayout.SOUTH, new JPanel());
        mainPanel.add(BorderLayout.EAST, new JPanel());
        //endregion

        //region ...set size and location of window
        this.pack();
        this.setSize(default_width, default_height);
        this.setResizable(false);
        Point location = (Point) this.getLocation();
        this.setLocation(location.x - getWidth() / 2, location.y - getHeight() / 2);
        //endregion

        //region ...set background image
        backImage = new JLabel(new ScaleIcon("img/back.png"));
        backImage.setBounds(0, 0, getWidth(), getHeight());
        this.getLayeredPane().setLayout(null);
        this.getLayeredPane().add(backImage, Integer.valueOf(Integer.MIN_VALUE));
        this.getLayeredPane().setOpaque(true);
        this.getLayeredPane().setBackground(Color.darkGray);
        setAllPanelOpaque((JPanel) this.getContentPane(), false);
        //endregion

        //程序由entry panel开始
        switchPanel("entry");

        //循环播放背景音乐
        try {
            bgm = new Sound(Sound.Path.BGM);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("bgm play, duration:" + bgm.getDuration());
                    bgm.play();
                }
            }, 1, bgm.getDuration());
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归设置所有panel的Opaque
     *
     * @param panel    需要设置的面板
     * @param isOpaque 是否不透明
     */
    private void setAllPanelOpaque(JPanel panel, boolean isOpaque) {
        panel.setOpaque(isOpaque);
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel temp = (JPanel) comp;
                temp.setOpaque(isOpaque);
                if (temp.getComponentCount() > 0) setAllPanelOpaque(temp, isOpaque);
            }
        }
    }

    public void scaleSize(int scale) {
        this.setSize(default_width * scale, default_height * scale);
        backImage.setBounds(0, 0, getWidth(), getHeight());
    }

    private void switchPanel(String panelName) {
        ((CardLayout) switchPanel.getLayout()).show(switchPanel, panelName);
        sceneName = panelName;
    }

    /**
     * 游戏开始入口，只由 entry panel 调用
     * 初始化 game panel 和 message panel
     *
     */
    private void gameStart() {
        //region ...reset gap of main panel
        {
            BorderLayout layout = (BorderLayout) mainPanel.getLayout();
            int vGap = (15 - getGameLevel()) * 5 + 2;
            double hGap = vGap * (getWidth() * 1.5 / getHeight());
            layout.setHgap((int) hGap);
            layout.setVgap(vGap);
        }
        //endregion

        messagePanel = new MessagePanel(getGameLevel());
        gamePanel = new GamePanel(getGameLevel());

        gamePanel.addLinkBlockListener(e -> {
            messagePanel.addBlockSource(1);
            if(messagePanel.isFinished()){
                gameOver();
            }
        });
        messagePanel.addTimeOutListener(e -> gameOver());

        mainPanel.add(BorderLayout.NORTH, messagePanel);
        mainPanel.add(BorderLayout.CENTER, gamePanel);

        setAllPanelOpaque(gamePanel, false);
        setAllPanelOpaque(messagePanel, false);

        messagePanel.startCountDown();
        switchPanel("play");
    }

    private void restartGame() {
        gamePanel.reset();
        messagePanel.reset();
        switchPanel("play");
    }

    private void gamePause() {
        //TODO 模态窗口+暂停计时器
        messagePanel.stopCountDown();
        JOptionPane.showMessageDialog(this,"游戏已暂停，点击继续进行游戏","暂停", JOptionPane.QUESTION_MESSAGE, new ImageIcon("img/b1.png"));
        messagePanel.startCountDown();
    }

    /**
     * 点击了菜单栏上的退出或结算页面的退出时
     *
     */
    private void gameExit() {
        if(!sceneName.equals("entry")) {
            mainPanel.remove(gamePanel);
            mainPanel.remove(messagePanel);
            gamePanel = null;
            messagePanel = null;
            switchPanel("entry");
        }
    }

    /**
     * 倒计时结束或者消除所有方块时
     *
     */
    private void gameOver() {
        if(sceneName.equals("play")) {
            overPanel.setLastTime(messagePanel.getLastTime());
            overPanel.setPoints(messagePanel.getSource());
            switchPanel("over");
        }
    }

    private int getGameLevel() {
        return gameLevel;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(GameClient::new);
    }
}