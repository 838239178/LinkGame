package gm.swing;

import gm.game.GameMap;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Block extends JComponent {
    private int id;
    private Image icon;
    private final Image background;
    private Point pointOnMap;
    private boolean selected;
    private final EventListenerList blockClickedListenerList;

    public Block(int id, Image icon) {
        this.id = id;
        this.icon = icon;
        this.background = new ImageIcon("img/b_back.png").getImage();
        this.blockClickedListenerList = new EventListenerList();
        this.addMouseListener(new MouseListenerInner());
    }

    public boolean isBlank(){
        return (id == GameMap.BLANK_BLOCK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if(!isBlank()) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(background, 0, 0, getWidth(), getHeight(), null);
            g2d.drawImage(icon, 10, 10, getWidth() - 20, getHeight() - 20, null);
            if (isSelected()) {
                g2d.setColor(Color.red);
                g2d.setStroke(new BasicStroke(getWidth() / 10.0f));
                g2d.drawRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    public void setPointOnMap(Point pointOnMap) {
        this.pointOnMap = pointOnMap;
    }

    public Point getPointOnMap() {
        return pointOnMap;
    }

    public void addBlockClickedListener(ActionListener l) {
        blockClickedListenerList.add(ActionListener.class, l);
    }

    public void clear() {
        id = GameMap.BLANK_BLOCK;
        icon = null;
        //test
        //System.out.println("block clear");
    }

    @Override
    public Point getLocation() {
        Point res = super.getLocation();
        //取中心点
        return new Point(res.x + getWidth() / 2, res.y + getHeight() / 2);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIcon(Image img) {
        this.icon = img;
    }

    public int getId() {
        return id;
    }

    private void invokeBlockClickedListener(ActionEvent e) {
        for (ActionListener listener : blockClickedListenerList.getListeners(ActionListener.class)) {
            listener.actionPerformed(e);
        }
    }

    private class MouseListenerInner extends MouseAdapter {
        private boolean onPressed = false;

        @Override
        public void mousePressed(MouseEvent e) {
            Rectangle rec = new Rectangle(getWidth(), getHeight());
            if (rec.contains(e.getPoint())) {
                onPressed = true;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (onPressed && !isBlank()) {
                setSelected(!isSelected());
                repaint();
                onPressed = false;
                invokeBlockClickedListener(new ActionEvent(e.getSource(), e.getID(), "click block"));
            }
        }
    }
}
