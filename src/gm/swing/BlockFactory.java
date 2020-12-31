package gm.swing;

import javax.swing.*;
import java.awt.*;

public enum BlockFactory {
    INSTANCE;

    public final int ID_MAX = 10;

    private final Image[] images;
    private final Image BLOCK_BACK;

    BlockFactory(){
        images = new Image[ID_MAX+1];
        BLOCK_BACK = new ImageIcon("img/block/b_back.png").getImage();
        for (int i = 1; i < images.length; i++) {
            images[i] = new ImageIcon("img/block/b" + i + ".png").getImage();
        }
    }

    public Block getBlock(int id){
        return new Block(id, images[id]);
    }

    /**
     * 重置方块，改变id和图片，避免创建对象的开销
     * @param src 需要重置的对象
     * @param id  目标id
     */
    public void resetBlock(Block src, int id){
        src.setIcon(images[id]);
        src.setId(id);
    }

    public Image getBackImg(){
        return BLOCK_BACK;
    }
}
