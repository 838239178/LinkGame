package gm.swing;

import gm.game.GameMap;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Block生成工厂类
 *
 * @author 施嘉宏
 */
public enum BlockFactory {
    INSTANCE;

    public final int ID_MAX = 10;
    public final String IMG_ROOT = "img/block/";

    private final Image[] images;
    private final Image BLOCK_BACK;
    private final Image SELECT_IMG;

    BlockFactory() {
        images = new Image[ID_MAX + 1];
        SELECT_IMG = new ImageIcon(IMG_ROOT+"select.png").getImage();
        BLOCK_BACK = new ImageIcon(IMG_ROOT + "b_back.png").getImage();

        images[0] = null;
        for (int i = 1; i < images.length; i++) {
            images[i] = new ImageIcon(IMG_ROOT + "b" + i + ".png").getImage();
        }
    }

    public void makeBlank(Block src) {
        src.setId(GameMap.BLANK_BLOCK);
        src.setIcon(null);
    }

    public Block getBlock(int id) {
        return new Block(id, images[id]);
    }

    /**
     * 重置方块，改变id和图片，避免创建对象的开销
     *
     * @param src 需要重置的对象
     * @param id  目标id
     */
    public void resetBlock(Block src, int id) {
        src.setIcon(images[id]);
        src.setId(id);
    }

    public Image getBackImg() {
        return BLOCK_BACK;
    }

    public Image getSelectImg(){
        return SELECT_IMG;
    }
}
