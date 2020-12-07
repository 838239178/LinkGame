package gm.swing;

//import gm.game.GameMap;

import gm.game.GameMap;
import gm.game.LinkResult;
import gm.game.LinkType;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class GamePanel extends JPanel {
    private GameMap map;
    private Block currentBlock1;
    private Block currentBlock2;
    private boolean drawLine;
    private final ArrayList<Point> linePoints;
    private final ArrayList<Block> blocks;
    private final EventListenerList linkBlockListeners;

    public GamePanel(int level) {
        //region ...default initial
        this.map = new GameMap();
        this.linkBlockListeners = new EventListenerList();
        this.blocks = new ArrayList<>();
        this.linePoints = new ArrayList<>();
        this.currentBlock1 = null;
        this.currentBlock2 = null;
        this.drawLine = false;
        this.setLayout(new GridLayout(level, level, 6, 6));
        //endregion

        //region ...initial blocks
        for(int y = 1; y<=level; y++){
            for(int x = 1; x<=level; x++){
                int id = map.getId(y,x);
                Block block = BlockFactory.INSTANCE.getBlock(id);
                block.setPositionOnMap(new Point(x,y));
                blocks.add(block);
            }
        }
        //endregion

        for (Block b : blocks) {
            b.addBlockClickedListener(e -> {
                Block current = (Block) e.getSource();

                if (currentBlock1 == null) currentBlock1 = current;
                else if (currentBlock2 == null) currentBlock2 = current;
                else {
                    current.setSelected(false);
                    return;
                }

                if (isSelectedDoubleBlock()) {
                    tryLinkBlocks();
                }
            });
            this.add(b);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (drawLine) {
            g2d.setStroke(new BasicStroke(8f));
            g2d.setColor(Color.red);
            for (int i = 0; i < linePoints.size() - 1; i++) {
                Line2D line = new Line2D.Double(linePoints.get(i), linePoints.get(i + 1));
                g2d.draw(line);
            }
        }
    }

    private Block findBlockByMapPoint(Point pointOnMap) {
        for (Block block : blocks) {
            if (block.getPositionOnMap().equals(pointOnMap)) return block;
        }
        return null;
    }

    private void startDrawLines(LinkResult link, int delay) {
        drawLine = true;
        try {
            switch (link.getLinkType()) {
                case SINGLE_CORNER_LINK:
                    linePoints.add(findBlockByMapPoint(link.getFirstPoint()).getLocation());
                    linePoints.add(findBlockByMapPoint(link.getFirstCorner()).getLocation());
                    linePoints.add(findBlockByMapPoint(link.getSecondPoint()).getLocation());
                    break;
                case DOUBLE_CORNER_LINK:
                    Point firstPoint = link.getFirstPoint();
                    Point secondPoint = link.getSecondPoint();
                    Point firstCorner = link.getFirstCorner();
                    Point secondCorner = link.getSecondCorner();
                    //需判断点和拐点是否在同一条线上
                    if(firstCorner.y != firstPoint.y && firstCorner.x != firstPoint.x){
                        firstCorner = secondCorner;
                        secondCorner = link.getFirstCorner();
                    }
                    linePoints.add(findBlockByMapPoint(firstPoint).getLocation());
                    linePoints.add(findBlockByMapPoint(firstCorner).getLocation());
                    linePoints.add(findBlockByMapPoint(secondCorner).getLocation());
                    linePoints.add(findBlockByMapPoint(secondPoint).getLocation());
                    break;
                case STRAIGHT_LINK:
                    linePoints.add(findBlockByMapPoint(link.getFirstPoint()).getLocation());
                    linePoints.add(findBlockByMapPoint(link.getSecondPoint()).getLocation());
                    break;
                default:
                    return;
            }

            repaint();

            //绘制线段，一段时间后清除方块和线段
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    drawLine = false;
                    currentBlock2.clear();
                    currentBlock1.clear();
                    cancelSelect();
                    linePoints.clear();
                    repaint();
                }
            },delay);

        } catch (NullPointerException e) {
            cancelSelect();
        }
    }

    private void tryLinkBlocks() {
        if (isSelectedDoubleBlock()) {
            LinkResult res = map.isLink(currentBlock1.getPositionOnMap(), currentBlock2.getPositionOnMap());
            if (res.getLinkType() != LinkType.NO_LINK) {
                LinkBlocks(res);
            } else {
                cancelSelect();
            }
        }
    }

    private void LinkBlocks(LinkResult link) {
        map.remove(currentBlock1.getPositionOnMap(), currentBlock2.getPositionOnMap());
        startDrawLines(link, 500);
        invokeLinkBlockListener(new ActionEvent(this, 0, "link blocks"));
    }

    private void invokeLinkBlockListener(ActionEvent e){
        for (ActionListener listener : linkBlockListeners.getListeners(ActionListener.class)) {
            listener.actionPerformed(e);
        }
    }

    private boolean isSelectedDoubleBlock() {
        return currentBlock1 != null && currentBlock2 != null;
    }

    private void cancelSelect() {
        if (currentBlock1 != null) currentBlock1.setSelected(false);
        if (currentBlock1 != null) currentBlock2.setSelected(false);
        currentBlock2 = currentBlock1 = null;
    }

    private void updateBlocks(){
        for (Block block : blocks) {
            int idOnMap = map.getId(block.getPositionOnMap().y,block.getPositionOnMap().x);
            if(block.getId() != idOnMap){
                BlockFactory.INSTANCE.resetBlock(block, idOnMap);
            }
        }
        repaint();
    }

    public void refreshBlocks(){
        map.refresh();
        updateBlocks();
    }

    private void tip(LinkResult link){
        try{
            Block block1 = findBlockByMapPoint(link.getFirstPoint());
            Block block2 = findBlockByMapPoint(link.getSecondPoint());
            block1.setSelected(true);
            block2.setSelected(true);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    block1.setSelected(false);
                    block2.setSelected(false);
                }
            }, 500);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void tipBlock(){
        LinkResult res = map.findLinkedPoint();
        tip(res);
    }

    public void reset(){
        this.map = new GameMap();
        cancelSelect();
        updateBlocks();
    }

    public void addLinkBlockListener(ActionListener l) {
        linkBlockListeners.add(ActionListener.class, l);
    }
}
