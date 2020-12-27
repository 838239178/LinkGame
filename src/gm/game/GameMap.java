package gm.game;

import java.awt.*;
import java.util.Random;

public class GameMap {
    public static final int BLANK_BLOCK = -1;

    public int returnID(Point p){return new Random().nextInt(5);}

    public void changeMap() {}

    public LinkResult isConnex(Point p1, Point p2) {return null;}

    public void remove(Point p1, Point p2) {}

    public LinkResult autoConnex(){return null;}
}
