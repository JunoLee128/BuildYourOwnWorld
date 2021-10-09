package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.Random;

import static byow.Core.Engine.WIDTH;
import static byow.Core.Engine.HEIGHT;

public class CreateWorld {

    public static TETile[][] makeWorld(Random random) {
        TETile[][] newWorld = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                newWorld[x][y] = Tileset.NOTHING;
            }
        }
        RoomGeneration.chunkGeneration(newWorld, random);
        return newWorld;
    }

}
