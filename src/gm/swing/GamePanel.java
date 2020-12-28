package gm.swing;

import gm.game.GameMap;
import gm.game.LinkResult;
import gm.game.LinkType;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GamePanel extends JPanel {
    private GameMap map;

    /**
     * 已选择的方块1
     */
    private Block currentBlock1;

    /**
     * 已选择的方块2
     */
    private Block currentBlock2;

    /**
     * 通知画线的标记位
     */
    private boolean drawLine;

    /**
     * 用于绘制连接线段的端点集合
     */
    private final ArrayList<Point> linePoints;

    /**
     * 所有方块的集合
     */
    private final ArrayList<Block> blocks;

    /**
     * 方块消除事件的监听器
     */
    private final EventListenerList linkBlockListeners;

    /**
     * 音频对象
     */
    private Sound linkSound;
    private Sound touchSound;

    /**
     * 游戏难度系数
     */
    private final int LEVEL;

    /**
     * GamePanel中的定时器
     */
    private final Timer timer;

    public GamePanel(int level) {
        //region ...default initial
        this.LEVEL = level;
        this.map = new GameMap(level);
        this.timer = new Timer();
        try {
            linkSound = new Sound(Sound.Path.LINK_SOUND);
            touchSound = new Sound(Sound.Path.TOUCH_SOUND);
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
            linkSound = null;
            touchSound = null;
        }
        this.linkBlockListeners = new EventListenerList();
        this.blocks = new ArrayList<>();
        this.linePoints = new ArrayList<>();
        this.currentBlock1 = null;
        this.currentBlock2 = null;
        this.drawLine = false;
        this.setLayout(new GridLayout(level+2, level+2, 1, 1));
        //endregion

        //region ...initial blocks
        //TODO 使用迭代器
        //test
        for (int y = 0; y < level+2; y++) {
            for (int x = 0; x < level+2; x++) {
                gm.game.Point p = new gm.game.Point(y,x);
                int id = map.returnID(p);
                Block block = BlockFactory.INSTANCE.getBlock(id);
                block.setPointOnMap(p);
                blocks.add(block);
            }
        }

        for (Block b : blocks) {
            b.addBlockClickedListener(e -> {
                touchSound.play();
                Block current = (Block) e.getSource();
                //如果正在画线，阻止任何响应
                if(drawLine) {
                    current.setSelected(false);
                    return;
                }

                //如果是反选，消除引用
                if (!current.isSelected()) {
                    if (current == currentBlock1) {
                        currentBlock1 = null;
                    } else if (current == currentBlock2) {
                        currentBlock2 = null;
                    }
                    return;
                }


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
        //endregion
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

    /**
     * 根据 game map 下标找到对应方块
     *
     * @param pointOnMap 在GameMap中的下标
     * @return 对应的Block对象，未找到返回null
     */
    private Block findBlockByMapPoint(Point pointOnMap) {
        /*
        可行方案
        game map 下标对应 block 数组下标 pointOnMap.y * n + pointOnMap.x
        return blocks[pointOnMap.y * n + pointOnMap.x]
        */
        for (Block block : blocks) {
            if (block.getPointOnMap().equals(pointOnMap)) return block;
        }
        return null;
    }


    /**
     * 获取相消方块的坐标参数、控制画线周期
     * 通知组件画线，并在一段时间后取消通知、清除点信息
     *
     * @param link  线的端点
     * @param delay 画线持续时间（ms)
     */
    private void startDrawLines(LinkResult link, int delay) {
        drawLine = true;
        try {
            Point firstPoint = link.getFirstPoint();
            Point secondPoint = link.getSecondPoint();
            Point firstCorner = link.getFirstCorner();
            Point secondCorner = link.getSecondCorner();
            switch (link.getLinkType()) {
                case SINGLE_CORNER_LINK:
                    linePoints.add(findBlockByMapPoint(firstPoint).getLocation());
                    linePoints.add(findBlockByMapPoint(firstCorner).getLocation());
                    linePoints.add(findBlockByMapPoint(secondPoint).getLocation());
                    break;
                case DOUBLE_CORNER_LINK:
                    //需判断点和拐点是否在同一条线上
                    if (firstCorner.y != firstPoint.y && firstCorner.x != firstPoint.x) {
                        firstCorner = secondCorner;
                        secondCorner = link.getFirstCorner();
                    }
                    linePoints.add(findBlockByMapPoint(firstPoint).getLocation());
                    linePoints.add(findBlockByMapPoint(firstCorner).getLocation());
                    linePoints.add(findBlockByMapPoint(secondCorner).getLocation());
                    linePoints.add(findBlockByMapPoint(secondPoint).getLocation());
                    break;
                case STRAIGHT_LINK:
                    linePoints.add(findBlockByMapPoint(firstPoint).getLocation());
                    linePoints.add(findBlockByMapPoint(secondPoint).getLocation());
                    break;
                default:
                    return;
            }

            linkSound.play();
            repaint();

            //一段时间后清除方块和线段
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    drawLine = false;
                    currentBlock2.clear();
                    currentBlock1.clear();
                    cancelSelect();
                    linePoints.clear();
                    repaint();
                }
            }, delay);

        } catch (NullPointerException e) {
            cancelSelect();
            drawLine = false;
        }
    }

    /**
     * 尝试消除两个方块
     * 若未选择两方块则不会执行, 若正在画线（上一轮未结束）取消选取
     * 执行后判断是否能够消除
     *
     * block listener -> tryLinkBlocks -> LinkBlocks -> startDrawLines
     */
    private void tryLinkBlocks() {
        if (isSelectedDoubleBlock()) {
            if(drawLine){
                cancelSelect();
                return;
            }
            LinkResult res = map.isConnex((gm.game.Point)currentBlock1.getPointOnMap(), (gm.game.Point)currentBlock2.getPointOnMap());
            if (res.getLinkType() != LinkType.NO_LINK) {
                LinkBlocks(res);
            } else {
                cancelSelect(150);
            }
        }
    }

    /**
     * 当方块连接成功后调用
     *
     * @param link 连接的结果
     */
    private void LinkBlocks(LinkResult link) {
        map.remove((gm.game.Point)currentBlock1.getPointOnMap(), (gm.game.Point)currentBlock2.getPointOnMap());
        startDrawLines(link, 400);
        invokeLinkBlockListener(new ActionEvent(this, 0, "link blocks"));
    }

    /**
     * 触发消除方块后调用
     *
     * @param e 触发事件
     */
    private void invokeLinkBlockListener(ActionEvent e) {
        for (ActionListener listener : linkBlockListeners.getListeners(ActionListener.class)) {
            listener.actionPerformed(e);
        }
    }

    /**
     * 是否进行了一组选择
     *
     * @return true if had chosen two different blocks
     */
    private boolean isSelectedDoubleBlock() {
        return currentBlock1 != null && currentBlock2 != null && currentBlock1 != currentBlock2;
    }

    /**
     * 立即取消选取并清除引用
     */
    private void cancelSelect() {
        if (currentBlock1 != null) currentBlock1.setSelected(false);
        if (currentBlock2 != null) currentBlock2.setSelected(false);
        currentBlock2 = currentBlock1 = null;
    }

    /**
     * 立即清除引用，但过一段时间再取消选取
     * @param delay 等待的时间（ms)
     */
    private void cancelSelect(int delay) {
        final Block temp1 = currentBlock1;
        final Block temp2 = currentBlock2;
        currentBlock2 = currentBlock1 = null;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(temp1 != null) temp1.setSelected(false);
                if(temp2 != null) temp2.setSelected(false);
            }
        },delay);
    }


    /**
     * 更新方块，方块分布发生变化时使用
     */
    private void updateBlocks() {
        for (Block block : blocks) {
            int idOnMap = map.returnID((gm.game.Point) block.getPointOnMap());
            if (block.getId() != idOnMap) {
                BlockFactory.INSTANCE.resetBlock(block, idOnMap);
            }
        }
        repaint();
    }

    /**
     * 重新随机方块位置
     */
    public void refreshBlocks() {
        map.changeMap();
        updateBlocks();
    }

    /**
     * 提示两个可以消除的方块，绘制提示动画
     * 提示动画：短暂地选择两个方块（不记录到currentBlock）
     *
     * @param link 提示方块构成的点的信息
     */
    private void tip(LinkResult link) {
        try {
            Block block1 = findBlockByMapPoint(link.getFirstPoint());
            Block block2 = findBlockByMapPoint(link.getSecondPoint());
            block1.setSelected(true);
            block2.setSelected(true);

            //提示一段时间后消除提示
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    block1.setSelected(false);
                    block2.setSelected(false);
                }
            }, 500);
        } catch (NullPointerException e) {
            System.out.println("tip failed, link result has null point");
            e.printStackTrace();
        }
    }

    /**
     * 提示两个可消除方块
     */
    public void tipBlock() {
        LinkResult res = map.autoConnex();
        if(res.getLinkType() != LinkType.NO_LINK) {
            tip(res);
        } else {
            System.out.println("tip: no link");
        }
    }

    /**
     * 重新生成游戏
     */
    public void reset() {
        this.map = new GameMap(LEVEL);
        cancelSelect();
        updateBlocks();
    }

    /**
     * 添加消除方块事件的监听器
     *
     * @param l 监听器
     */
    public void addLinkBlockListener(ActionListener l) {
        linkBlockListeners.add(ActionListener.class, l);
    }
}
