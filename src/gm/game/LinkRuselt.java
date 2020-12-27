public class LinkRuselt {
    private Point firstPoint = null;
    private Point secondPoint = null;
    private Point firstCorner = null;
    private Point secondCorner = null;
    private LinkType linkType = null;

    public LinkRuselt() {
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

    public LinkType getLinkType() {
        return linkType;
    }

    public void setLinkType(LinkType linkType) {
        this.linkType = linkType;
    }
}
