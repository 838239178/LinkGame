package gm.swing;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * 游戏初始界面，负责启动开始游戏前的准备工作
 * 负责功能：
 * 1.设定难度 2.开始游戏 3.退出程序
 *
 * @author 施嘉宏
 */
public class EntryPanel extends JPanel {
    private JButton[] levelButton;
    private JButton startBtn;
    private JButton exitBtn;
    private JButton returnBtn;
    private JPanel switchPanel;
    private JPanel levelPanel;
    private JPanel enterPanel;
    private JPanel titlePanel;

    private final String SCENE_LEVEL = "level";
    private final String SCENE_ENTER = "enter";

    private final EventListenerList LevelChangeListenerList;
    private final EventListenerList clientExitListenerList;

    private Sound touchSound;

    public EntryPanel() {
        try {
            touchSound = new Sound(Sound.Path.TOUCH_SOUND);
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
            touchSound = null;
        }

        //region ...initial components
        Icon defaultIcon = new ScaleIcon("img/button_default.png");
        Icon pressedIcon = new ScaleIcon("img/button_pressed.png");
        Icon rolloverIcon = new ScaleIcon("img/button_rollover.png");
        LevelChangeListenerList = new EventListenerList();
        clientExitListenerList = new EventListenerList();
        levelButton = new JButton[3];
        levelButton[0] = new IconButton(defaultIcon, pressedIcon, rolloverIcon, GameClient.TEXT_EASY);
        levelButton[1] = new IconButton(defaultIcon, pressedIcon, rolloverIcon, GameClient.TEXT_NORM);
        levelButton[2] = new IconButton(defaultIcon, pressedIcon, rolloverIcon, GameClient.TEXT_HARD);
        exitBtn = new IconButton(defaultIcon, pressedIcon, rolloverIcon, "退出游戏");
        startBtn = new IconButton(defaultIcon, pressedIcon, rolloverIcon, "开始游戏");
        returnBtn = new IconButton(defaultIcon, pressedIcon, rolloverIcon, "返回");
        titlePanel = new JPanel(new BorderLayout(15, 0));
        levelPanel = new JPanel(new GridLayout(4, 1, 1, 5));
        enterPanel = new JPanel(new GridLayout(2, 1, 1, 50));
        switchPanel = new JPanel(new CardLayout(10, 10));

        Font font1 = new Font("楷体", Font.BOLD, 30);

        startBtn.setFont(font1);
        startBtn.setFocusPainted(false);

        exitBtn.setFont(font1);
        exitBtn.setFocusPainted(false);

        returnBtn.setFont(font1);
        returnBtn.setFocusPainted(false);

        for (JButton button : levelButton) {
            button.setFocusPainted(false);
            button.setFont(font1);
        }
        //endregion

        //region ...add action listener
        returnBtn.addActionListener(e-> {
            touchSound.play();
            switchScene(SCENE_ENTER);
        });
        startBtn.addActionListener(e-> {
            touchSound.play();
            switchScene(SCENE_LEVEL);
        });
        exitBtn.addActionListener(e-> {
            touchSound.play();
            invokeClientExitListener(e);
            System.exit(0);
        });
        for (JButton button : levelButton) {
            button.addActionListener(e -> {
                touchSound.play();
                invokeLevelChangeListener(e);
            });
        }
        //endregion

        //region ...add components
        for (JButton button : levelButton) {
            levelPanel.add(button);
        }
        levelPanel.add(returnBtn);
        titlePanel.add(BorderLayout.CENTER, new JLabel(new ScaleIcon("img/title.png", ScaleIcon.HEIGHT_FIXED, ScaleIcon.ALIGN_CENTER)));
        enterPanel.add(startBtn);
        enterPanel.add(exitBtn);
        switchPanel.add(enterPanel, SCENE_ENTER);
        switchPanel.add(levelPanel, SCENE_LEVEL);

        JPanel tempPanel = new JPanel(new BorderLayout(250,20));
        tempPanel.add(BorderLayout.CENTER, switchPanel);
        tempPanel.add(BorderLayout.WEST, new JPanel());
        tempPanel.add(BorderLayout.EAST, new JPanel());
        tempPanel.add(BorderLayout.SOUTH, new JPanel());

        this.setLayout(new GridLayout(0,1,1,50));
        this.add(titlePanel);
        this.add(tempPanel);
        //endregion

        switchScene("entry");
    }

    private void switchScene(String scene) {
        ((CardLayout) switchPanel.getLayout()).show(switchPanel, scene);
    }

    /**
     * 触发难度选择事件
     *
     * @param e ActionEvent 其中ID规定为难度系数
     */
    private void invokeLevelChangeListener(ActionEvent e) {
        for (ActionListener listener : LevelChangeListenerList.getListeners(ActionListener.class)) {
            int level = GameClient.toDifficulty(e.getActionCommand());
            listener.actionPerformed(new ActionEvent(e.getSource(), level, e.getActionCommand()));
        }
        switchScene(SCENE_ENTER);
    }

    private void invokeClientExitListener(ActionEvent e) {
        for (ActionListener listener : clientExitListenerList.getListeners(ActionListener.class)) {
            listener.actionPerformed(e);
        }
    }

    /**
     * 当难度选择后触发
     *
     * @param l action listener
     */
    public void addLevelChangeListener(ActionListener l) {
        LevelChangeListenerList.add(ActionListener.class, l);
    }

    public void addClientExitListener(ActionListener l) {
        clientExitListenerList.add(ActionListener.class, l);
    }

}
