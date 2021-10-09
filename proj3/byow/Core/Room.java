package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;

public class Room implements Serializable {

    public static final int MINSIZE = 4;
    public static final int MAXSIZE = 20;
    private int posX; //bottom left corner coords?
    private int posY;
    private int sizeX;
    private int sizeY;

    public Room(Point point, int sizeX, int sizeY) {
        this.posX = point.getX();
        this.posY = point.getY();
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    /**Sets tiles in world.
     * Borders are walls, inside is floor.
     */
    public void generateRoom(TETile[][] world) {
        //bottom wall
        for (int i = posX; i <= posX + sizeX; i++) {
            world[i][posY] = Tileset.WALL;
        }
        //everything in the middle
        for (int i = posY + 1; i < posY + sizeY; i++) { //row
            world[posX][i] = Tileset.WALL;
            world[posX + sizeX][i] = Tileset.WALL;
            for (int j = posX + 1; j < posX + sizeX; j++) {
                world[j][i] = Tileset.FLOOR;
            }
        }
        //top wall
        for (int i = posX; i <= posX + sizeX; i++) {
            world[i][posY + sizeY] = Tileset.WALL;
        }
    }

    public int getX1() {
        return this.posX;
    }

    public int getX2() {
        return this.posX + this.sizeX;
    }

    public int getY1() {
        return this.posY;
    }

    public int getY2() {
        return this.posY + this.sizeY;
    }

    public int getSizeX() {
        return this.sizeX;
    }

    public int getSizeY() {
        return this.sizeY;
    }

    public Point getBase() {
        return new Point(this.posX, this.posY);
    }

    public boolean isIntersecting(Room otherRoom) {
        return getX1() < otherRoom.getX2()
                && getX2() > otherRoom.getX1()
                && getY1() < otherRoom.getY2()
                && getY2() > otherRoom.getY1();
    }

}
