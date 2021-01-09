package gm.swing;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

/**
 * 游戏入口类
 * 确定整体逻辑
 * 游戏由EntryPanel开始，从EntryPanel中结束。
 * EntryPanel->GamePanel->OverPanel->(EntryPanel/GamePanel)
 *
 * @author 施嘉宏
 */
public class GameClient extends JFrame {
    public final static int HARD = 10;
    public final static int NORM = 8;
    public final static int EASY = 6;

    public final static String TEXT_EASY = "简单";
    public final static String TEXT_NORM = "中等";
    public final static String TEXT_HARD = "困难";

    public final String PLAY_SCENE = "play";
    public final String ENTRY_SCENE = "entry";
    public final String OVER_SCENE = "over";

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
    private TimerTask bgmPlayTask;

    public GameClient() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setTitle("连连看");

        //region ...initial panels
        aboutDialog = new AboutDialog(this);
        switchPanel = new JPanel(new CardLayout());
        mainPanel = new JPanel(new BorderLayout(1, 1));
        entryPanel = new EntryPanel();
        overPanel = new OverPanel();
        menuBar = new JMenuBar();

        entryPanel.addLevelChangeListener(e -> {
            //规定从event的id中获取难度系数
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
            JMenu soundMenu = new JMenu("Sound");
            JMenuItem restart = new JMenuItem("Restart");
            JMenuItem refresh = new JMenuItem("Refresh");
            JMenuItem tips = new JMenuItem("Tips");
            JMenuItem quit = new JMenuItem("Quit");
            JMenuItem pause = new JMenuItem("Pause");

            gameMenu.setMnemonic(KeyEvent.VK_G);
            settingMenu.setMnemonic(KeyEvent.VK_S);

            quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
            quit.addActionListener(e -> gameExit());

            resizeMenu.add("restore").addActionListener(e -> scaleSize(1,1));
            resizeMenu.add("large").addActionListener(e -> scaleSize(1.28, 1.28));

            soundMenu.add("bgm off").addActionListener(e -> stopBGM());
            soundMenu.add("bgm on").addActionListener(e -> playBGM());

            settingMenu.add(soundMenu);
            settingMenu.add(resizeMenu);
            settingMenu.add("about").addActionListener(e -> aboutDialog.setVisible(true));

            restart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
            restart.addActionListener(e -> restartGame());

            refresh.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
            refresh.addActionListener(e -> {
                if (isOnScene(PLAY_SCENE)) {
                    messagePanel.addRefreshCount(1);
                    GlobalThreadPool.INSTANCE.submit(() -> gamePanel.refreshBlocks());
                }
            });

            tips.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK));
            tips.addActionListener(e -> {
                if (isOnScene(PLAY_SCENE)) {
                    messagePanel.addTipCount(1);
                    GlobalThreadPool.INSTANCE.submit(() -> gamePanel.tipBlock());
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
        switchPanel.add(mainPanel, PLAY_SCENE);
        switchPanel.add(entryPanel, ENTRY_SCENE);
        switchPanel.add(overPanel, OVER_SCENE);

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
        Point location = this.getLocation();
        this.setLocation(location.x - getWidth() / 2, location.y - getHeight() / 2);
        //endregion

        //region ...set background image
        backImage = new JLabel(new ScaleIcon("img/back.png"));
        backImage.setBounds(0, 0, getWidth(), getHeight());
        this.getLayeredPane().setLayout(null);
        this.getLayeredPane().add(backImage, Integer.valueOf(Integer.MIN_VALUE));
        this.getLayeredPane().setOpaque(true);
        this.getLayeredPane().setBackground(Color.darkGray);
        GlobalThreadPool.INSTANCE.submit(()-> setAllPanelOpaque((JPanel) this.getContentPane(), false));
        //endregion

        //程序由entry panel开始
        switchScene(ENTRY_SCENE);

        try {
            bgm = new Sound(Sound.Path.BGM);
            playBGM();
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }


    public void playBGM() {
        //循环播放背景音乐
        if(bgm == null) return;
        if(bgm.isPlaying()) return;
        bgmPlayTask = new TimerTask() {
            @Override
            public void run() {
                bgm.play();
            }
        };
        new Timer().schedule(bgmPlayTask, 1, bgm.getDuration());
    }

    public void stopBGM(){
        if(bgm == null) return;
        if(!bgm.isPlaying()) return;
        bgm.stop();
        bgmPlayTask.cancel();
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

    public void scaleSize(double scaleWid, double scaleHei) {
        this.setSize(default_width * scaleWid, default_height * scaleHei);
        backImage.setBounds(0, 0, getWidth(), getHeight());
    }

    public void setSize(double width, double height) {
        this.setSize((int) width, (int) height);
    }

    private void switchScene(String scene) {
        ((CardLayout) switchPanel.getLayout()).show(switchPanel, scene);
        sceneName = scene;
    }

    /**
     * 游戏开始入口，只由 entry panel 调用
     * 初始化 game panel 和 message panel
     */
    private void gameStart() {
        messagePanel = new MessagePanel(getGameLevel());
        gamePanel = new GamePanel(getGameLevel());

        gamePanel.addLinkBlockListener(e -> {
            messagePanel.addBlockSource(1);
            if (messagePanel.isFinished()) {
                gameOver();
            }
        });
        messagePanel.addTimeOutListener(e -> gameOver());

        mainPanel.add(BorderLayout.NORTH, messagePanel);
        mainPanel.add(BorderLayout.CENTER, gamePanel);

        setAllPanelOpaque(gamePanel, false);
        setAllPanelOpaque(messagePanel, false);

        messagePanel.startCountDown();
        switchScene(PLAY_SCENE);
    }

    private void restartGame() {
        if (isOnScene(PLAY_SCENE) || isOnScene(OVER_SCENE)) {
            gamePanel.reset();
            messagePanel.reset();
            switchScene(PLAY_SCENE);
        }
    }

    private void gamePause() {
        if (isOnScene(PLAY_SCENE)) {
            messagePanel.stopCountDown();
            JOptionPane.showMessageDialog(this, "游戏已暂停，点击继续进行游戏", "暂停", JOptionPane.QUESTION_MESSAGE, new ImageIcon("img/pauseIcon.png"));
            messagePanel.startCountDown();
        }
    }

    /**
     * 点击了菜单栏上的退出或结算页面的返回时
     */
    private void gameExit() {
        if (!isOnScene(ENTRY_SCENE)) {
            messagePanel.stopCountDown();
            mainPanel.remove(gamePanel);
            mainPanel.remove(messagePanel);
            gamePanel = null;
            messagePanel = null;
            switchScene(ENTRY_SCENE);
        }
    }

    /**
     * 倒计时结束或者消除所有方块时
     */
    private void gameOver() {
        if (isOnScene(PLAY_SCENE)) {
            messagePanel.stopCountDown();
            overPanel.setSpendTime(messagePanel.getSpendTime());
            overPanel.setWin(messagePanel.getLastTime() > 0);
            overPanel.setPoints(messagePanel.getSource());
            //等待最后的动画播放完整
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    switchScene(OVER_SCENE);
                }
            }, 200);
        }
    }

    public boolean isOnScene(String sceneName) {
        return this.sceneName.equals(sceneName);
    }

    public int getGameLevel() {
        return gameLevel;
    }

    public static int toDifficulty(String difficultyText) {
        return switch (difficultyText) {
            case TEXT_EASY -> GameClient.EASY;
            case TEXT_HARD -> GameClient.HARD;
            case TEXT_NORM -> GameClient.NORM;
            default -> throw new NoSuchElementException("no such difficulty");
        };
    }

    public static String toDifficulty(int difficultyDegree) {
        return switch (difficultyDegree) {
            case EASY -> TEXT_EASY;
            case NORM -> TEXT_NORM;
            case HARD -> TEXT_HARD;
            default -> throw new NoSuchElementException("no such difficulty");
        };
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(GameClient::new);
    }
}
