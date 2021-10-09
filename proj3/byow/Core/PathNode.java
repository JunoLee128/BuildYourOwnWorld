package byow.Core;

//Nodes used in the pathfinding algorithm
public class PathNode implements Comparable<PathNode> {

    private final Point point;
    private int fCost;
    private int gCost;
    private int hCost;
    private PathNode parent;

    public PathNode(Point position) {
        point = position;
    }

    public Point getPoint() {
        return point;
    }

    public void setHCost(int cost) {
        hCost = cost;
    }

    public int getHCost() {
        return hCost;
    }

    public int getFCost() {
        return fCost;
    }

    public void setFCost() {
        fCost = gCost + hCost;
    }

    public void setFCost(int cost) {
        fCost = cost;
    }

    public int getGCost() {
        return gCost;
    }

    public void setGCost(int cost) {
        gCost = cost;
    }

    public void setParent(PathNode node) {
        parent = node;
    }

    public PathNode getParent() {
        return parent;
    }

    public int[] getArrayPoint() {
        return new int[]{point.getX(), point.getY()};
    }

    @Override
    public int compareTo(PathNode node) {
        return fCost - node.getFCost();
    }

}
