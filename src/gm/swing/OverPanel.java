package gm.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class OverPanel extends JPanel {
    private JLabel point;
    private JLabel time;

    private final JButton exit;
    private final JButton restart;

    public OverPanel() {
        this.setLayout(new BorderLayout(20,1));

        JPanel container = new JPanel(new BorderLayout(50,1));
        JPanel labelPanel1 = new JPanel(new GridLayout(4,1,5,20));
        JPanel labelPanel2 = new JPanel(new GridLayout(4,1,5,20));
        JPanel buttonPanel = new JPanel(new GridLayout(1,5,20,20));
        JPanel titlePanel = new JPanel(new GridLayout(1,3,1,1));
        JLabel pointLabel = new JLabel("最终得分：");
        JLabel timeLabel = new JLabel("花费时间：");
        point = new JLabel("10002200");
        time = new JLabel("1000000");
        exit = new JButton("返回");
        restart = new JButton("重试");

        Font font = new Font("黑体", Font.BOLD, 50);

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
        labelPanel2.add(time);
        labelPanel2.add(point);
        labelPanel2.add(new JPanel());

        buttonPanel.add(new JPanel());
        buttonPanel.add(restart);
        buttonPanel.add(new JPanel());
        buttonPanel.add(exit);
        buttonPanel.add(new JPanel());

        titlePanel.add(new JPanel());
        titlePanel.add(new JLabel(new ScaleIcon("img/result.png")));
        titlePanel.add(new JPanel());

        container.add(BorderLayout.WEST, labelPanel1);
        container.add(BorderLayout.CENTER, labelPanel2);
        container.add(BorderLayout.SOUTH, buttonPanel);
        container.add(BorderLayout.NORTH, titlePanel);

        this.add(BorderLayout.CENTER, container);
        this.add(BorderLayout.WEST, new JPanel());
        this.add(BorderLayout.SOUTH, new JPanel());
        this.add(BorderLayout.EAST, new JPanel());
        this.add(BorderLayout.NORTH, new JPanel());
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

    public void setLastTime(int lastTime) {
        this.time.setText(String.valueOf(lastTime));
    }
}
