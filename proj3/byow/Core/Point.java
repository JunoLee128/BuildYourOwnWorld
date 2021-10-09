package byow.Core;

import byow.TileEngine.TETile;

import java.io.Serializable;

public class Point implements Serializable {

    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TETile getTile(TETile[][] world) {
        return world[x][y];
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", getX(), getY());
    }

    public double distanceTo(Point other) {
        return Math.sqrt((x - other.getX()) * (x - other.getX())
                + (y - other.getY()) * (y - other.getY()));
    }

}
