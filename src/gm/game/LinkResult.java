package gm.game;

import java.awt.*;

public class LinkResult {
    private LinkType linkType;
    private Point firstPoint;
    private Point secondPoint;
    private Point firstCorner;
    private Point secondCorner;

    public LinkType getLinkType() {
        return linkType;
    }

    public void setLinkType(LinkType linkType) {
        this.linkType = linkType;
    }

    public Point getFirstPoint() {
        return firstPoint;
    }

    public void setFirstPoint(Point firstPoint) {
        this.firstPoint = firstPoint;
    }

    public Point getSecondPoint() {
        return secondPoint;
    }

    public void setSecondPoint(Point secondPoint) {
        this.secondPoint = secondPoint;
    }

    public Point getFirstCorner() {
        return firstCorner;
    }

    public void setFirstCorner(Point firstCorner) {
        this.firstCorner = firstCorner;
    }

    public Point getSecondCorner() {
        return secondCorner;
    }

    public void setSecondCorner(Point secondCorner) {
        this.secondCorner = secondCorner;
    }
}
