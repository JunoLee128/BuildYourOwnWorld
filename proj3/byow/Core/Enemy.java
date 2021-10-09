package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;

import static byow.Core.PathFinding.*;
import static edu.princeton.cs.introcs.StdDraw.show;

public class Enemy implements Serializable {

    private Point currentPosition;
    private int HP;
    private Point currentTarget;
    private ArrayList<Point> currentPath = null;
    private boolean alive;
    private int pathIndex;
    private boolean isShroom;
    private boolean wasGoal;

    public Enemy(Point startingPosition, int startingHP, Player player, TETile[][] world) {
        currentPosition = startingPosition;
        HP = startingHP;
        currentTarget = player.getPosition();
        currentPath = findPath(currentPosition, currentTarget, world, player);
        pathIndex = 1;
        alive = true;
        isShroom = false;
        wasGoal = false;
    }

    public boolean isAlive() {
        return alive;
    }

    public void takeTurn(Player player, TETile[][] world,
                         TERenderer teRenderer, GameRunner gameRunner) {
        if (currentPosition.distanceTo(player.getPosition()) < 1.5) {
            hitAndDie(player, world, teRenderer, gameRunner);
            return;
        }
        if (makeNewPath(player.getPosition())) {
            currentPath = findPath(currentPosition, currentTarget, world, player);
            pathIndex = 1;
        }
        if (pathIndex >= currentPath.size()) {
            currentPath = findPath(currentPosition, currentTarget, world, player);
            pathIndex = 1;
        } else {
            Point moveTo = currentPath.get(pathIndex);
            TETile desiredTile = world[moveTo.getX()][moveTo.getY()];
            if (desiredTile.equals(Tileset.TROLL)) {
                return;
            }
            if (wasGoal) {
                world[currentPosition.getX()][currentPosition.getY()] = Tileset.GOAL;
                wasGoal = false;
            } else {
                world[currentPosition.getX()][currentPosition.getY()] = Tileset.FLOOR;
            }
            if (desiredTile.equals(Tileset.GOAL)) {
                wasGoal = true;
            }
            currentPosition = moveTo;
            generateEntity(world);
            pathIndex += 1;
        }
    }

    private void hitAndDie(Player player, TETile[][] world,
                           TERenderer teRenderer, GameRunner gameRunner) {
        if (teRenderer != null) {
            for (int i = 0; i < 6; i++) {
                hitPlayer(world, player, teRenderer);
            }
        }
        player.takeDamage(world, 1, teRenderer, gameRunner);
        die(world, teRenderer);
    }

    private void hitPlayer(TETile[][] world, Player player, TERenderer teRenderer) {
        world[currentPosition.getX()][currentPosition.getY()] = Tileset.FLOOR;
        world[player.getPosition().getX()][player.getPosition().getY()] = Tileset.TROLL;
        renderSafe(teRenderer, world);
        renderSafe(teRenderer, world);
        world[currentPosition.getX()][currentPosition.getY()] = Tileset.TROLL;
        world[player.getPosition().getX()][player.getPosition().getY()] = Tileset.DAMAGED;
        renderSafe(teRenderer, world);
        renderSafe(teRenderer, world);
    }

    public void addToPath(Player player, TETile[][] world) {
        currentPath.add(player.getPosition());
        currentTarget = player.getPosition();
    }

    private boolean makeNewPath(Point targetPoint) {
        if (currentPath == null) {
            return true;
        }
        int distanceTravelled = Math.abs(currentTarget.getX() - targetPoint.getX())
                + Math.abs(currentTarget.getY() - targetPoint.getY());
        if (distanceTravelled > 20) {
            return true;
        }
        int distanceToTarget = Math.abs(targetPoint.getX() - currentPosition.getX())
                + Math.abs(targetPoint.getY() - currentPosition.getY());
        if (distanceToTarget <= 10) {
            return true;
        }
        return false;
    }

    public void takeDamage(TETile[][] world, int damage, TERenderer teRenderer) {
        HP = HP - damage;
        if (HP <= 0) {
            die(world, teRenderer);
        }
    }

    public void die(TETile[][] world, TERenderer teRenderer) {
        world[currentPosition.getX()][currentPosition.getY()] = Tileset.FLOOR;
        renderSafe(teRenderer, world);
        world[currentPosition.getX()][currentPosition.getY()] = Tileset.TROLL;
        renderSafe(teRenderer, world);
        world[currentPosition.getX()][currentPosition.getY()] = Tileset.FLOOR;
        renderSafe(teRenderer, world);
        world[currentPosition.getX()][currentPosition.getY()] = Tileset.TROLL;
        renderSafe(teRenderer, world);
        world[currentPosition.getX()][currentPosition.getY()] = Tileset.FLOOR;
        renderSafe(teRenderer, world);

        alive = false;
    }

    public void generateEntity(TETile[][] world) {
        int posX = currentPosition.getX();
        int posY = currentPosition.getY();
        if (isShroom) {
            world[posX][posY] = Tileset.MUSHROOM;
        } else {
            world[posX][posY] = Tileset.TROLL;
        }
    }

    public Point getPosition() {
        return null;
    }

    public int gethP() {
        return 0;
    }

    public void showPath(TETile[][] world, Player player, TERenderer teRenderer) {
        currentPath = findPath(currentPosition, currentTarget, world, player);
        for (int i = 0; i < currentPath.size(); i++) {
            TETile thisTile = world[currentPath.get(i).getX()][currentPath.get(i).getY()];
            if (thisTile.equals(Tileset.PLAYER) || thisTile.equals(Tileset.TROLL)
                    || thisTile.equals(Tileset.GOAL) || thisTile.equals(Tileset.MUSHROOM)) {
                continue;
            }
            world[currentPath.get(i).getX()][currentPath.get(i).getY()] = Tileset.FLOWER;
            renderSafe(teRenderer, world);
        }
        for (int i = 0; i < currentPath.size(); i++) {
            TETile thisTile = world[currentPath.get(i).getX()][currentPath.get(i).getY()];
            if (thisTile.equals(Tileset.PLAYER) || thisTile.equals(Tileset.TROLL)
                    || thisTile.equals(Tileset.GOAL) || thisTile.equals(Tileset.MUSHROOM)) {
                continue;
            }
            world[currentPath.get(i).getX()][currentPath.get(i).getY()] = Tileset.GRASS;
            renderSafe(teRenderer, world);
        }
        for (int i = 0; i < currentPath.size(); i++) {
            TETile thisTile = world[currentPath.get(i).getX()][currentPath.get(i).getY()];
            if (thisTile.equals(Tileset.PLAYER) || thisTile.equals(Tileset.TROLL)
                    || thisTile.equals(Tileset.GOAL) || thisTile.equals(Tileset.MUSHROOM)) {
                continue;
            }
            world[currentPath.get(i).getX()][currentPath.get(i).getY()] = Tileset.FLOOR;
        }
    }

    public void makeShroom(TETile[][] world) {
        isShroom = true;
        generateEntity(world);
    }

    public void revertShroom(TETile[][] world) {
        isShroom = false;
        generateEntity(world);
    }

    private void renderSafe(TERenderer teRenderer, TETile[][] world) {
        if (teRenderer != null) {
            teRenderer.renderFrame(world);
            show();
        }
    }
}
