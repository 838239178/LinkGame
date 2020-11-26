package gm.swing;

import gm.game.GameMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Block extends JComponent {
    private int id;
    private Image icon;
    private Point positionOnMap;
    private boolean selected;

    public Block(int id, Image icon) {
        this.id = id;
        this.icon = icon;
        this.addMouseListener(new MouseListenerInner());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(icon, 0, 0, getWidth(), getHeight(), null);
        if(isSelected()){
            g2d.setColor(Color.red);
            g2d.setStroke(new BasicStroke(10.0f));
            g2d.drawRect(0,0,getWidth()-1,getHeight()-1);
        }
    }

    public void setPositionOnMap(Point positionOnMap) {
        this.positionOnMap = positionOnMap;
    }
    

    public void clear() {
        id = GameMap.BLANK_BLOCK;
        icon = null;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    private class MouseListenerInner extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            Point location = getLocation();
            System.out.println("click" + location);
            setSelected(!isSelected());
            repaint();
        }
    }
}
