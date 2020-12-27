package gm.swing;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class EntryPanel extends JPanel {
    private JButton[] levelButton;
    private JButton startBtn;
    private JButton exitBtn;
    private JButton returnBtn;
    private JPanel switchPanel;
    private JPanel levelPanel;
    private JPanel enterPanel;
    private JPanel titlePanel;
    private final EventListenerList LevelChangeListenerList;
    private final EventListenerList clientExitListenerList;

    private Sound touchSound;

    public EntryPanel(){
        this.setLayout(new BorderLayout(250,100));
        try {
            touchSound = new Sound(Sound.Path.TOUCH_SOUND);
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
            touchSound = null;
        }

        //region ...initial components
        LevelChangeListenerList = new EventListenerList();
        clientExitListenerList = new EventListenerList();
        levelButton = new JButton[3];
        levelButton[0] = new JButton("简单");
        levelButton[1] = new JButton("中等");
        levelButton[2] = new JButton("困难");
        exitBtn = new JButton("退出游戏");
        startBtn = new JButton("开始游戏");
        returnBtn = new JButton("返回");
        titlePanel = new JPanel(new BorderLayout(30,0));
        levelPanel = new JPanel(new GridLayout(4,1,5,5));
        enterPanel = new JPanel(new GridLayout(2,1,5,50));
        switchPanel = new JPanel(new CardLayout(10,10));

        Font font1 = new Font("楷体", Font.BOLD, 25);

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
            switchPanel("enter");
        });
        startBtn.addActionListener(e-> {
            touchSound.play();
            switchPanel("level");
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
        titlePanel.add(BorderLayout.EAST, new JPanel());
        titlePanel.add(BorderLayout.WEST, new JPanel());
        titlePanel.add(BorderLayout.CENTER, new JLabel(new ImageIcon("img/title.png")));
        enterPanel.add(startBtn);
        enterPanel.add(exitBtn);
        switchPanel.add(enterPanel, "enter");
        switchPanel.add(levelPanel,"level");
        this.add(BorderLayout.CENTER, switchPanel);
        this.add(BorderLayout.NORTH, titlePanel);
        this.add(BorderLayout.WEST, new JPanel());
        this.add(BorderLayout.EAST, new JPanel());
        this.add(BorderLayout.SOUTH, new JPanel());
        //endregion

        switchPanel("entry");
    }

    private void switchPanel(String panelName){
        ((CardLayout)switchPanel.getLayout()).show(switchPanel, panelName);
    }

    private void invokeLevelChangeListener(ActionEvent e){
        for (ActionListener listener : LevelChangeListenerList.getListeners(ActionListener.class)) {
            int level = switch (e.getActionCommand()){
                case "简单" -> GameClient.EASY;
                case "困难" -> GameClient.DIFF;
                case "中等" -> GameClient.MED;
                default -> 0;
            };
            listener.actionPerformed(new ActionEvent(e.getSource(), level, e.getActionCommand()));
        }
        switchPanel("enter");
    }

    private void invokeClientExitListener(ActionEvent e){
        for (ActionListener listener : clientExitListenerList.getListeners(ActionListener.class)) {
            listener.actionPerformed(e);
        }
    }

    public void addLevelChangeListener(ActionListener l){
        LevelChangeListenerList.add(ActionListener.class ,l);
    }
    public void addClientExitListener(ActionListener l){
        clientExitListenerList.add(ActionListener.class ,l);
    }

}