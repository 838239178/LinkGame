package gm.swing;

import gm.game.GameMap;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * 可复选的组件
 * 点击一次触发Click事件
 *
 * @author 施嘉宏
 */
public class Block extends JComponent {
    private int id;
    private Image icon;
    private final Image select;
    private final Image background;
    private Point pointOnMap;
    private boolean selected;
    private boolean drawSelected;       //可用于非选择性绘制选择框。仅用于绘制，无其他任何效果。
    private final EventListenerList blockClickedListenerList;

    public Block(int id, Image icon) {
        this.id = id;
        this.icon = icon;
        this.select = BlockFactory.INSTANCE.getSelectImg();
        this.background = BlockFactory.INSTANCE.getBackImg();
        this.blockClickedListenerList = new EventListenerList();
        this.addMouseListener(new MouseListenerInner());
    }

    public boolean isBlank(){
        return (id == GameMap.BLANK_BLOCK);
    }

    public void setDrawSelected(boolean isDraw) {
        this.drawSelected = isDraw;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if(!isBlank()) {
            Graphics2D g2d = (Graphics2D) g;
            //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(background, 0, 0, getWidth(), getHeight(), null);
            g2d.drawImage(icon, 10, 10, getWidth() - 20, getHeight() - 20, null);
            if (isSelected() || drawSelected) {
                g2d.drawImage(select,0,0,getWidth(), getHeight(),null);
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

    /**
     * 重置自身为空方块
     *
     */
    public void clear() {
        BlockFactory.INSTANCE.makeBlank(this);
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
//        private boolean onPressed = false;
//
//        @Override
//        public void mousePressed(MouseEvent e) {
//            Rectangle rec = new Rectangle(getWidth(), getHeight());
//            if (rec.contains(e.getPoint())) {
//                onPressed = true;
//            }
//        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (!isBlank()) {
                setSelected(!isSelected());
                repaint();
//                onPressed = false;
                invokeBlockClickedListener(new ActionEvent(e.getSource(), e.getID(), "click block"));
            }
        }
    }
}
