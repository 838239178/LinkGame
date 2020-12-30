package gm.swing;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.Timer;

import javax.swing.event.EventListenerList;

public class MessagePanel extends JPanel {
    public static final Color MY_GREEN = new Color(93, 191, 8);
    public static final Color MY_RED = new Color(243, 68, 68);

    private final JLabel sourceLabel;
    private final JLabel title;
    private final JProgressBar timeBar;
    private final EventListenerList timeOutListenerList;

    private final int LEVEL;
    private int blockSource;
    private int refreshCounts;
    private int tipCounts;

    //region ...计时器组成部分
    private final JLabel lastTimeLabel;
    private final int TIME;
    private int lastTime;
    Timer timer;
    TimerTask task;
    //endregion

    public MessagePanel(int level) {
        this.LEVEL = level;
        int time = getTimeLimit(level);
        this.timeOutListenerList = new EventListenerList();
        this.TIME = time;
        this.lastTime = time;
        this.blockSource = 0;
        this.setLayout(new BorderLayout(20, 10));

        timer = new Timer();

        JPanel barPanel = new JPanel(new BorderLayout(30, 15));
        timeBar = new JProgressBar(0, time);
        lastTimeLabel = new JLabel("Last Time");
        sourceLabel = new JLabel("得分: 10");
        title = new JLabel(new ScaleIcon("img/title_small.png"));


        lastTimeLabel.setFont(new Font("微软雅黑", Font.BOLD, 15));
        lastTimeLabel.setForeground(Color.lightGray);

        sourceLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        sourceLabel.setForeground(Color.WHITE);

        title.setForeground(Color.WHITE);
        title.setFont(new Font("楷体", Font.BOLD, 40));

        timeBar.setValue(time);
        timeBar.setForeground(MessagePanel.MY_GREEN);
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
        barPanel.add(BorderLayout.NORTH, new JPanel());
        barPanel.add(BorderLayout.NORTH, new JPanel());
    }

    /**
     * 开始计时，不会重置计时器内容
     * 倒计时结束后清除计时器
     */
    public void startCountDown() {
        task = new TimerTask() {
            @Override
            public void run() {
                timeBar.setValue(lastTime--);
                if (lastTime == 0) {
                    invokeTimeOutListener(new ActionEvent(this, 0, "time out"));
                    this.cancel();
                }
                if (lastTime < TIME / 4) timeBar.setForeground(MessagePanel.MY_RED);
                lastTimeLabel.setText("剩余时间：" + " " + lastTime + "s");
                sourceLabel.setText("得分: " + getSource());
                repaint();
            }
        };
        timer.schedule(task, 0, 1000);
    }

    /**
     * 停止计时，不会重置计时器内容
     */
    public void stopCountDown() {
        task.cancel();
    }

    /**
     * 重置计时器并重新开始计时
     */
    public void resetCountDown() {
        stopCountDown();
        lastTime = TIME;
        timeBar.setValue(TIME);
        timeBar.setForeground(MessagePanel.MY_GREEN);
        startCountDown();
    }

    private int getTimeLimit(int level) {
        return switch (level) {
            case GameClient.HARD -> 120;
            case GameClient.NORM -> 90;
            case GameClient.EASY -> 40;
            default -> 0;
        };
    }

    public int getLastTime() {
        return lastTime;
    }

    public int getSpendTime() {
        return TIME - getLastTime();
    }

    public int getSource() {
        //得分公式
        return (int) ((blockSource * LEVEL * LEVEL) / ((getSpendTime() / (LEVEL*LEVEL / 32.0)) * (refreshCounts + 1) * (tipCounts * 5 + 1) * 0.5));
    }

    public void reset() {
        this.blockSource = 0;
        resetCountDown();
    }

    public void addBlockSource(int source) {
        blockSource += source;
    }

    public boolean isFinished() {
        return blockSource >= LEVEL * LEVEL / 2;
    }

    public void addRefreshCount(int count) {
        refreshCounts += count;
    }

    public void addTipCount(int count) {
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
