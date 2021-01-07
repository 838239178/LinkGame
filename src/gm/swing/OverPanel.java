package gm.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

/**
 * 游戏结束时绘制，负责显示最终结果，可重新开始游戏或返回EntryPanel。
 *
 * @author 施嘉宏
 */
public class OverPanel extends JPanel {
    private final JLabel point;
    private final JLabel time;

    private final JLabel resultLabel;

    private final JButton exit;
    private final JButton restart;

    private boolean isWin;

    public OverPanel() {
        this.setLayout(new BorderLayout(20, 1));

        JPanel container = new JPanel(new BorderLayout(50, 10));
        JPanel labelPanel1 = new JPanel(new GridLayout(4, 1, 5, 20));
        JPanel labelPanel2 = new JPanel(new GridLayout(4, 1, 5, 20));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 1, 1));
        JPanel titlePanel = new JPanel(new GridLayout(1, 3, 20, 1));
        JLabel pointLabel = new JLabel("最终得分：");
        JLabel timeLabel = new JLabel("花费时间：");
        Icon defaultIcon = new ScaleIcon("img/button_default.png");
        Icon pressedIcon = new ScaleIcon("img/button_pressed.png");
        Icon rolloverIcon = new ScaleIcon("img/button_rollover.png");
        resultLabel = new JLabel();
        point = new JLabel("");
        time = new JLabel("");
        exit = new IconButton(defaultIcon, pressedIcon, rolloverIcon, "返回");
        restart = new IconButton(defaultIcon, pressedIcon, rolloverIcon, "重试");

        Font font = new Font("黑体", Font.BOLD, 50);

        exit.setFont(new Font("楷体", Font.BOLD, 30));
        restart.setFont(new Font("楷体", Font.BOLD, 30));

        resultLabel.setFont(font);
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pointLabel.setFont(font);
        pointLabel.setForeground(Color.WHITE);
        pointLabel.setVerticalAlignment(SwingConstants.CENTER);

        timeLabel.setFont(font);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setVerticalAlignment(SwingConstants.CENTER);

        time.setFont(font);
        time.setForeground(Color.ORANGE);

        point.setFont(font);
        point.setForeground(Color.ORANGE);

        labelPanel1.add(new JPanel());
        labelPanel1.add(pointLabel);
        labelPanel1.add(timeLabel);
        labelPanel1.add(new JPanel());

        labelPanel2.add(new JPanel());
        labelPanel2.add(point);
        labelPanel2.add(time);
        labelPanel2.add(new JPanel());

        buttonPanel.add(new JPanel());
        buttonPanel.add(restart);
        buttonPanel.add(new JPanel());
        buttonPanel.add(exit);
        buttonPanel.add(new JPanel());

        titlePanel.add(resultLabel);

        container.add(BorderLayout.WEST, labelPanel1);
        container.add(BorderLayout.CENTER, labelPanel2);
        container.add(BorderLayout.SOUTH, buttonPanel);
        container.add(BorderLayout.NORTH, titlePanel);

        this.add(BorderLayout.CENTER, container);
        this.add(BorderLayout.WEST, new JPanel());
        this.add(BorderLayout.SOUTH, new JPanel());
        this.add(BorderLayout.EAST, new JPanel());
        this.add(BorderLayout.NORTH, new JPanel());

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                if(isWin){
                    resultLabel.setIcon(new ScaleIcon("img/success.png", ScaleIcon.HEIGHT_FIXED, ScaleIcon.ALIGN_CENTER));
                } else {
                    resultLabel.setIcon(new ScaleIcon("img/defeat.png", ScaleIcon.HEIGHT_FIXED, ScaleIcon.ALIGN_CENTER));
                }
            }
        });
    }

    public void addExitActonListener(ActionListener l){
        exit.addActionListener(l);
    }

    public void addRestartActonListener(ActionListener l){
        restart.addActionListener(l);
    }

    public void setPoints(int points) {
        this.point.setText(String.valueOf(points));
    }

    public void setSpendTime(int spendTime) {
        this.time.setText(spendTime + " S");
    }

    public void setWin(boolean isWin){
        this.isWin = isWin;
    }
}
