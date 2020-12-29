package gm.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class OverPanel extends JPanel {
    private final JLabel point;
    private final JLabel time;

    private final JLabel resultLabel;

    private final JButton exit;
    private final JButton restart;

    public OverPanel() {
        this.setLayout(new BorderLayout(20,1));

        JPanel container = new JPanel(new BorderLayout(50,1));
        JPanel labelPanel1 = new JPanel(new GridLayout(4,1,5,20));
        JPanel labelPanel2 = new JPanel(new GridLayout(4,1,5,20));
        JPanel buttonPanel = new JPanel(new GridLayout(1,5,20,20));
        JPanel titlePanel = new JPanel(new GridLayout(1,3,20,1));
        JLabel pointLabel = new JLabel("最终得分：");
        JLabel timeLabel = new JLabel("花费时间：");
        Icon defaultIcon  = new ScaleIcon("img/button_default.png");
        Icon pressedIcon = new ScaleIcon("img/button_pressed.png");
        resultLabel = new JLabel();
        point = new JLabel("");
        time = new JLabel("");
        exit = new IconButton(defaultIcon, pressedIcon, defaultIcon,"返回");
        restart = new IconButton(defaultIcon, pressedIcon, defaultIcon,"重试");

        Font font = new Font("黑体", Font.BOLD, 50);

        exit.setFont(new Font("宋体", Font.BOLD, 20));
        restart.setFont(new Font("宋体", Font.BOLD, 20));

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

        //titlePanel.add(new JLabel());
        titlePanel.add(resultLabel);
        //titlePanel.add(new JLabel());

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

    public void setSpendTime(int spendTime) {
        this.time.setText(spendTime + " S");
    }

    public void setWin(boolean isWin){
        if(isWin){
//            resultLabel.setText("成功");
//            resultLabel.setForeground(MessagePanel.MY_GREEN)；
            resultLabel.setIcon(new ImageIcon("img/success.png"));
        } else {
//            resultLabel.setText("失败");
//            resultLabel.setForeground(MessagePanel.MY_RED);
            resultLabel.setIcon(new ImageIcon("img/defeat.png"));
        }
    }
}
