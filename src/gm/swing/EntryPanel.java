package gm.swing;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EntryPanel extends JPanel {
    private JButton[] levelButton;
    private JButton startBtn;
    private JButton exitBtn;
    private JButton returnBtn;
    private JPanel switchPanel;
    private JPanel levelPanel;
    private JPanel enterPanel;
    private final EventListenerList LevelChangeListenerList;
    private final EventListenerList clientExitListenerList;

    public EntryPanel(){
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
        levelPanel = new JPanel(new GridLayout(4,1,5,5));
        enterPanel = new JPanel(new GridLayout(2,1,5,5));
        switchPanel = new JPanel(new CardLayout(10,10));
        //endregion

        //region ...add action listener
        returnBtn.addActionListener(e-> switchPanel("enter"));
        startBtn.addActionListener(e-> switchPanel("level"));
        exitBtn.addActionListener(this::invokeGameExitListener);
        for (JButton button : levelButton) {
            button.addActionListener(this::invokeLevelChangeListener);
        }
        //endregion

        //region ...add components
        for (JButton button : levelButton) {
            levelPanel.add(button);
        }
        levelPanel.add(returnBtn);
        enterPanel.add(startBtn);
        enterPanel.add(exitBtn);
        switchPanel.add(enterPanel, "enter");
        switchPanel.add(levelPanel,"level");
        this.add(switchPanel);
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
            listener.actionPerformed(new ActionEvent(this, level, e.getActionCommand()));
        }
        switchPanel("enter");
    }

    private void invokeGameExitListener(ActionEvent e){
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
