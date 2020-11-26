package gm.swing;

import javax.swing.*;
import java.awt.*;

public enum BlockFactory {
    INSTANCE;

    public Block getBlock(int id){
        //test
        Image img = new ImageIcon("img//test.png").getImage();
        return new Block(id, img);
    }

}
