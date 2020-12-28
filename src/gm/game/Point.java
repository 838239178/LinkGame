package gm.game;

public class Point extends java.awt.Point {

    public Point(int x, int y) {
        super(x, y);
    }

    public Point() {
    }

    public int getx() {
        return (int)getX();
    }

    public void setX(int x) {
        this.x = x;
    }

    public int gety() {
        return (int)getY();
    }

    public void setY(int y) {
        this.y = y;
    }
}
