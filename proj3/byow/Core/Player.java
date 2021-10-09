package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;

public class Player implements Serializable {

    private int hP;
    private Point currentPosition;

    public Player(Point startingPosition, int hP) {
        this.hP = hP;
        this.currentPosition = startingPosition;
    }


    /* For move animation, check if valid tile to move to first
       Then, change current tile to be the same tile with player 50% leaving
       Change the next tile to be the same tile with player 50* entering.
       Change the current tile to be just the floor
       Change the next tile to the player over the floor.
       Make the next tile the current tile
     */
    public void move(Direction direction, int amount, TETile[][] world, GameRunner gameRunner) {
        Point transformedPoint = direction.transform(currentPosition, amount);
        TETile desiredTile = world[transformedPoint.getX()][transformedPoint.getY()];
        if (desiredTile.equals(Tileset.FLOOR) || desiredTile.equals(Tileset.GOAL)) {
            world[currentPosition.getX()][currentPosition.getY()] = Tileset.FLOOR;
            currentPosition = transformedPoint;
            generateEntity(world);
        }
        if (desiredTile.equals(Tileset.GOAL)) {
            gameRunner.touchedGoal();
        }
    }

    public void takeDamage(TETile[][] world, int damage,
                           TERenderer teRenderer, GameRunner gameRunner) {
        hP = hP - damage;
        if (hP <= 0) {
            die(world, teRenderer, gameRunner);
        } else {
            world[currentPosition.getX()][currentPosition.getY()] = Tileset.PLAYER;
        }
    }

    public void die(TETile[][] world, TERenderer teRenderer, GameRunner gameRunner) {
        world[currentPosition.getX()][currentPosition.getY()] = Tileset.FLOOR;
        renderSafe(teRenderer, world);
        world[currentPosition.getX()][currentPosition.getY()] = Tileset.PLAYER;
        renderSafe(teRenderer, world);
        world[currentPosition.getX()][currentPosition.getY()] = Tileset.FLOOR;
        renderSafe(teRenderer, world);
        world[currentPosition.getX()][currentPosition.getY()] = Tileset.PLAYER;
        renderSafe(teRenderer, world);
        world[currentPosition.getX()][currentPosition.getY()] = Tileset.FLOOR;
        renderSafe(teRenderer, world);
        gameRunner.gameOver();
    }

    public void generateEntity(TETile[][] world) {
        int posX = currentPosition.getX();
        int posY = currentPosition.getY();
        world[posX][posY] = Tileset.PLAYER;
    }

    public Point getPosition() {
        return currentPosition;
    }

    public int gethP() {
        return hP;
    }

    private void renderSafe(TERenderer teRenderer, TETile[][] world) {
        if (teRenderer != null) {
            teRenderer.renderFrame(world);
        }
    }
}
