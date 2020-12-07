package gm.game;

import java.awt.*;
import java.util.Random;

public class GameMap {
    public static final int BLANK_BLOCK = -1;

    public int getId(int y, int x){return new Random().nextInt(5);}

    public void refresh() {}

    public LinkResult isLink(Point p1, Point p2) {return null;}

    public void remove(Point p1, Point p2) {}

    public LinkResult findLinkedPoint(){return null;}
}
