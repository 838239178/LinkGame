package gm.swing;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.Timer;

import javax.swing.event.ChangeListener;

public class MessagePanel extends JPanel {
    private final JLabel label;
    private final JLabel title;
    private final JProgressBar timeBar;

    private final int TIME;
    private int lastTime;

    public MessagePanel(int time) {
        this.TIME = time;
        this.lastTime = time;
        this.setLayout(new BorderLayout(5, 5));

        JPanel temp = new JPanel(new GridLayout(1, 4, 1, 5));

        label = new JLabel("Last Time");
        title = new JLabel("LinkLinkLook");
        timeBar = new JProgressBar(0, time);

        timeBar.setValue(time);
        timeBar.setStringPainted(true);

        this.add(BorderLayout.WEST, title);
        this.add(BorderLayout.CENTER, temp);
        this.add(BorderLayout.NORTH, new JPanel());
        this.add(BorderLayout.EAST, new JPanel());
        this.add(BorderLayout.SOUTH, new JPanel());

        temp.add(new JPanel());
        temp.add(label);
        temp.add(timeBar);
        temp.add(new JPanel());


    }

    public void startCountDown() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                SwingUtilities.invokeLater(() -> {
                    timeBar.setValue(lastTime--);
                    repaint();
                });
            }
        }, 0, 1000);
    }

    public void addTimeBarChangeListener(ChangeListener cl) {
        timeBar.addChangeListener(cl);
    }

}
