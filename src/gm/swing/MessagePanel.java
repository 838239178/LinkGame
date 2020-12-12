package gm.swing;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.Timer;

import javax.swing.event.EventListenerList;

public class MessagePanel extends JPanel {
    public static final Color myGreen = new Color(93, 191, 8);
    public static final Color myRed = new Color(243, 68, 68, 255);

    private final JLabel lastTimeLabel;
    private final JLabel sourceLabel;
    private final JLabel title;
    private final JProgressBar timeBar;
    private final EventListenerList timeOutListenerList;

    private final int TIME;
    private int lastTime;
    private int blockSource;
    private int refreshCounts;
    private int tipCounts;

    public MessagePanel(int level) {
        int time = getTimeLimit(level);
        this.timeOutListenerList = new EventListenerList();
        this.TIME = time;
        this.lastTime = time;
        this.blockSource = 0;
        this.setLayout(new BorderLayout(20, 10));

        JPanel barPanel = new JPanel(new BorderLayout(30, 1));
        timeBar = new JProgressBar(0, time);
        lastTimeLabel = new JLabel("Last Time");
        sourceLabel = new JLabel("得分: 10");
        title = new JLabel("连连看");


        lastTimeLabel.setFont(new Font("微软雅黑", Font.BOLD, 15));
        lastTimeLabel.setForeground(Color.lightGray);

        sourceLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        sourceLabel.setForeground(Color.WHITE);

        title.setForeground(Color.WHITE);
        title.setFont(new Font("楷体", Font.BOLD, 40));

        timeBar.setValue(time);
        timeBar.setForeground(MessagePanel.myGreen);
        timeBar.setBackground(Color.gray);
        timeBar.setStringPainted(false);

        this.add(BorderLayout.WEST, title);
        this.add(BorderLayout.CENTER, barPanel);
        this.add(BorderLayout.NORTH, new JPanel());
        this.add(BorderLayout.EAST, new JPanel());
        this.add(BorderLayout.SOUTH, new JPanel());

        barPanel.add(BorderLayout.WEST, lastTimeLabel);
        barPanel.add(BorderLayout.CENTER, timeBar);
        barPanel.add(BorderLayout.EAST, sourceLabel);
    }

    public void startCountDown() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeBar.setValue(lastTime--);
                if (lastTime == 0) invokeTimeOutListener(new ActionEvent(this, 0, "time out"));
                if (lastTime < TIME / 4) timeBar.setForeground(MessagePanel.myRed);

                lastTimeLabel.setText("剩余时间：" + " " + lastTime + "s");
                sourceLabel.setText("得分: " + getSource());
                repaint();
            }
        }, 1, 1000);
    }

    private int getTimeLimit(int level) {
        return switch (level) {
            case GameClient.DIFF -> 120;
            case GameClient.MED -> 90;
            case GameClient.EASY -> 30;
            default -> 0;
        };
    }

    public int getLastTime() {
        return lastTime;
    }

    public int getSource() {
        //消去方块分最多为(n*10)分，在此基础上除以(消耗时间*重置次数*提示次数*0.1)。
        return (int) (blockSource * 10 / (TIME - lastTime) * refreshCounts * tipCounts * 0.1);
    }

    public void reset() {
        this.blockSource = 0;
        lastTime = TIME;
        timeBar.setValue(TIME);
        timeBar.setForeground(MessagePanel.myGreen);
    }

    public void addBlockSource(int source) {
        blockSource += source;
    }

    public void addRefreshCount(int count) {
        refreshCounts += count;
    }

    public void addTipCount(int count){
        tipCounts += count;
    }

    private void invokeTimeOutListener(ActionEvent e) {
        for (ActionListener listener : timeOutListenerList.getListeners(ActionListener.class)) {
            listener.actionPerformed(e);
        }
    }

    public void addTimeOutListener(ActionListener l) {
        timeOutListenerList.add(ActionListener.class, l);
    }

}
