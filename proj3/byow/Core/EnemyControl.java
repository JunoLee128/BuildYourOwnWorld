package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public class EnemyControl {

    public static Enemy spawnEnemy(Player player, ArrayList<Chunk> roomChunkList,
                                   Random random, TETile[][] world) {
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
        if (spawnPoint.distanceTo(player.getPosition()) < 10) {
            return spawnEnemy(player, roomChunkList, random, world);
        } else {
            return new Enemy(spawnPoint, 1, player, world);
        }
    }

}
