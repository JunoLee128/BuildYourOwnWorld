package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public class PlayerControl {

    public static Player spawnPlayer(ArrayList<Chunk> roomChunkList,
                                     Random random, TETile[][] world,
                                     GameRunner gameRunner, TERenderer ter) {
        int chosenChunkIndex = RandomUtils.uniform(random, 0, roomChunkList.size());
        Chunk chosenChunk = roomChunkList.get(chosenChunkIndex);
        Room chosenRoom = chosenChunk.getRoom();
        Point roomFloorBase = new Point(chosenRoom.getX1() + 1, chosenRoom.getY1() + 1);
        Point roomFloorBound = new Point(chosenRoom.getX2(), chosenRoom.getY2());
        Point spawnPoint = null;
        TETile spawnTile = null;
        while (spawnTile == null) {
            int randomX = RandomUtils.uniform(random, roomFloorBase.getX(), roomFloorBound.getX());
            int randomY = RandomUtils.uniform(random, roomFloorBase.getY(), roomFloorBound.getY());
            TETile potentialTile = world[randomX][randomY];
            if (potentialTile == Tileset.FLOOR) {
                spawnTile = potentialTile;
                spawnPoint = new Point(randomX, randomY);
            }
        }
        return new Player(spawnPoint, 3);
    }

    /**
     * Deprecated method.
     * @param key
     * @param player
     * @param world
     */


}
