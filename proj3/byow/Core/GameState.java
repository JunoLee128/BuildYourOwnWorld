package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Random;

/**
 * Solely for serialization (saving) purposes.
 */

public class GameState implements Serializable {

    private static final File CWD = new File(System.getProperty("user.dir"));
    private static final File SAVEFILE = Utils.join(CWD, "savefile.txt");
    private char[][] charWorld;
    private Player player;
    private Random random;
    private List<Enemy> enemyList;
    private List<Chunk> roomChunkList;
    private int touchedGoal;
    private int freezeDuration;
    private int freezeCooldown;

    public GameState(TETile[][] world, Player player, Random random, List<Enemy> enemyList,
         List<Chunk> roomChunkList, int touchedGoal, int freezeDuration, int freezeCooldown) {
        this.player = player;
        this.random = random;
        this.charWorld = new char[world.length][world[0].length];
        this.enemyList = enemyList;
        this.roomChunkList = roomChunkList;
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                charWorld[i][j] = world[i][j].character();
            }
        }
        this.touchedGoal = touchedGoal;
        this.freezeDuration = freezeDuration;
        this.freezeCooldown = freezeCooldown;
    }

    public void saveState() {
        try {
            Utils.writeObject(SAVEFILE, this);
        } catch (IOException e) {
            System.out.println("exception");
        }
    }

    /**
     * Load from file
     */
    public static GameState loadState() {
        if (SAVEFILE.exists()) {
            return Utils.readObject(SAVEFILE, GameState.class);
        } else {
            return null;
        }
    }

    public TETile[][] getWorld() {
        TETile[][] world = new TETile[charWorld.length][charWorld[0].length];
        for (int i = 0; i < charWorld.length; i++) {
            for (int j = 0; j < charWorld[0].length; j++) {
                TETile tile = tileFromChar(charWorld[i][j]);
                world[i][j] = tile;
            }
        }
        return world;
    }

    public Player getPlayer() {
        return player;
    }

    public Random getrandom() {
        return random;
    }

    public List<Enemy> getEnemyList() {
        return enemyList;
    }

    private TETile tileFromChar(char c) {
        switch (c) {
            case '@':
                return Tileset.PLAYER;
            case '#':
                return Tileset.WALL;
            case '·':
                return Tileset.FLOOR;
            case ' ':
                return Tileset.NOTHING;
            case '"':
                return Tileset.GRASS;
            case '=':
                return Tileset.TROLL;
            case '/':
                return Tileset.SEARCHED_FLOOR;
            case '*':
                return Tileset.GOAL;
            case '&':
                return Tileset.MUSHROOM;
            default:
                return null;
        }
    }

    public List<Chunk> getRoomChunkList() {
        return roomChunkList;
    }

    public int getTouchedGoal() {
        return touchedGoal;
    }

    public int getFreezeDuration() {
        return freezeDuration;
    }

    public int getFreezeCooldown() {
        return freezeCooldown;
    }

}
