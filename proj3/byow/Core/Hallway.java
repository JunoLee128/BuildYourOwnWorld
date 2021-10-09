package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class Hallway {

    private Point p1;
    private Point p2;
    private int width;

    public Hallway(int x1, int y1, int x2, int y2, int width) {
        this(new Point(x1, y1), new Point(x2, y2), width);
    }

    public Hallway(Point p1, Point p2, int width) {
        this.p1 = p1;
        this.p2 = p2;
        this.width = width;
    }

    //method to check if legal? maybe put it in main class?

    private void swap() {
        Point temp = p1;
        p1 = p2;
        p2 = temp;
    }

    private Point findCorner(Point po1, Point po2) {
        return new Point(po1.getX(), po2.getY());
    }

    /**
     * For some calculations generating paths/hallways.
     */
    private int getDirection(int a, int b) {
        if (a > b) {
            return 1;
        } else if (a < b) {
            return -1;
        }
        return 0;
    }

    /**
     * Generates a 1-wide bent "path" using TILE from (x1, y1) to (x2, y2).
     * Skips any tile for which TILEEVALUATOR returns false.
     * @param world
     */
    private void generatePath(Point po1, Point po2, TETile tile,
                              TileEvaluator tileEvaluator, TETile[][] world) {
        Point corner = findCorner(po1, po2);
        int directionY = getDirection(po2.getY(), po1.getY());
        for (int y = po1.getY(); y != corner.getY() + directionY; y += directionY) {
            if (tileEvaluator.eval(world[corner.getX()][y])) {
                world[corner.getX()][y] = tile;
            }
        }
        int directionX = getDirection(po2.getX(), po1.getX());
        for (int x = po1.getX(); x != po2.getX() + directionX; x += directionX) {
            if (tileEvaluator.eval(world[x][po2.getY()])) {
                world[x][po2.getY()] = tile;
            }
        }
    }

    public boolean isValid(TETile[][] world) {
        TileEvaluator validHallwayPoint = new TileEvaluator(true, Tileset.WALL, Tileset.NOTHING);
        if (width == 0) {
            return validHallwayPoint.eval(world[p1.getX()][p1.getY()])
                    && validHallwayPoint.eval(world[p2.getX()][p2.getY()]);
        } else {
            int directionX = getDirection(p2.getX(), p1.getX());
            if (directionX == 0) {
                directionX = 1;
            }
            int directionY = getDirection(p1.getY(), p2.getY());
            if (directionY == 0) {
                directionY = 1;
            }
            int xCoord = p1.getX() - directionX;
            int yCoord = p2.getY() - directionY;
            Point pathp1 = new Point(xCoord, p1.getY());
            Point pathp2 = new Point(p2.getX(), yCoord);
            return validHallwayPoint.eval(world[p1.getX()][p1.getY()])
                    && validHallwayPoint.eval(world[pathp1.getX()][pathp1.getY()])
                    && validHallwayPoint.eval(world[p2.getX()][p2.getY()])
                    && validHallwayPoint.eval(world[pathp2.getX()][pathp2.getY()]);
        }
    }

    public void generateHallway(TETile[][] world) {
        int directionX = getDirection(p2.getX(), p1.getX());
        if (directionX == 0) {
            directionX = 1;
        }
        int directionY = getDirection(p1.getY(), p2.getY());
        if (directionY == 0) {
            directionY = 1;
        }
        int xCoord = p1.getX() - directionX;
        int yCoord = p2.getY() - directionY;
        TileEvaluator wallValid = new TileEvaluator(Tileset.NOTHING);
        TileEvaluator floorValid = new TileEvaluator(Tileset.NOTHING, Tileset.WALL);
        Point pathp1 = new Point(xCoord, p1.getY());
        Point pathp2 = new Point(p2.getX(), yCoord);
        generatePath(pathp1, pathp2, Tileset.WALL, wallValid, world);
        for (int i = 0; i <= width; i++) {
            xCoord += directionX;
            yCoord += directionY;
            pathp1 = new Point(xCoord, p1.getY());
            pathp2 = new Point(p2.getX(), yCoord);
            generatePath(pathp1, pathp2, Tileset.FLOOR, floorValid, world);
        }
        xCoord += directionX;
        yCoord += directionY;
        pathp1 = new Point(xCoord, p1.getY());
        pathp2 = new Point(p2.getX(), yCoord);
        generatePath(pathp1, pathp2, Tileset.WALL, wallValid, world);
    }

    private TETile randomTile() {
        int num = new Random().nextInt(5);
        if (num == 0) {
            return Tileset.FLOWER;
        } else if (num == 1) {
            return Tileset.AVATAR;
        } else if (num == 2) {
            return Tileset.GRASS;
        } else if (num == 3) {
            return Tileset.MOUNTAIN;
        } else {
            return Tileset.SAND;
        }
    }
}
