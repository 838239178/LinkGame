package gm.swing;

import gm.game.GameMap;

import javax.imageio.ImageIO;
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
    private Point positionOnMap;
    private boolean selected;
    private boolean isBlank;
    private final EventListenerList blockClickedListenerList;

    public Block(int id, Image icon) {
        this.id = id;
        this.isBlank = false;
        this.icon = icon;
        this.background = new ImageIcon("img/b_back.png").getImage();
        this.blockClickedListenerList = new EventListenerList();
        this.addMouseListener(new MouseListenerInner());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(background,0,0,getWidth(), getHeight(),null);
        g2d.drawImage(icon, 10, 10,getWidth()-20, getHeight()-20, null);
        if (isSelected()) {
            g2d.setColor(Color.red);
            g2d.setStroke(new BasicStroke(getWidth() / 10.0f));
            g2d.drawRect(0, 0, getWidth(), getHeight());
        }
    }

    public void setPositionOnMap(Point positionOnMap) {
        this.positionOnMap = positionOnMap;
    }

    public Point getPositionOnMap() {
        return positionOnMap;
    }

    public void addBlockClickedListener(ActionListener l) {
        blockClickedListenerList.add(ActionListener.class, l);
    }

    public void clear() {
        id = GameMap.BLANK_BLOCK;
        icon = null;
        isBlank = true;
    }

    @Override
    public Point getLocation() {
        Point res = super.getLocation();
        return new Point(res.x + getWidth() / 2, res.y + getHeight() / 2);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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
        private boolean choose = false;

        @Override
        public void mousePressed(MouseEvent e) {
            Rectangle rec = new Rectangle(getWidth(), getHeight());
            if (rec.contains(e.getPoint())) {
                choose = true;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (choose && !isBlank) {
                setSelected(!isSelected());
                repaint();
                choose = false;
                invokeBlockClickedListener(new ActionEvent(e.getSource(), e.getID(), "click block"));
            }
        }
    }
}
