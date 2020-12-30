package gm.swing;

import javax.swing.*;
import java.awt.*;

public enum BlockFactory {
    INSTANCE;

    public Block getBlock(int id){
        Image img = new ImageIcon("img/block/b"+id+".png").getImage();
        return new Block(id, img);
    }

    public void resetBlock(Block src, int id){
        Image img = new ImageIcon("img/block/b"+id+".png").getImage();
        src.setIcon(img);
        src.setId(id);
    }

    public Image getBackImg(){
        return new ImageIcon("img/block/b_back.png").getImage();
    }
}
