package byow.Core;

import byow.TileEngine.TETile;

import java.util.ArrayList;
import java.util.Random;

public class HallwayGeneration {
    /* Generate a hallway in all the rooms.
       Using the chunkRoom list, we connect each chunk to its neighbor chunk via rooms & hallways
       Pick a random spot within each room, and connect the rooms to each other.
       The +2 and -1 offsets are to keep the chosen point inside room, rather than choosing a wall.
       After picking 2 spots, use the generateHallway method for each hallway to connect them.
     */
    private static final double WIDECHANCE = 0.2;

    public static void generateHallway(ArrayList<Chunk> chunkRooms,
                                       TETile[][] world, Random random) {
        for (int i = 0; i < chunkRooms.size() - 1; i++) {
            Room room1 = chunkRooms.get(i).getRoom();
            Room room2 = chunkRooms.get(i + 1).getRoom();
            connectRooms(room1, room2, world, random);
        }
    }

    public static void generateHallwaysNew(Chunk chunk, TETile[][] world, Random random) {
        Chunk left = chunk.getLeftChunk();
        Chunk right = chunk.getRightChunk();
        if (left != null & right != null) {
            Room room1 = left.getRandomRoom(random);
            Room room2 = right.getRandomRoom(random);
            connectRooms(room1, room2, world, random);
            generateHallwaysNew(left, world, random);
            generateHallwaysNew(right, world, random);
        }
    }



    /**
     * Randomly creates a hallway between ROOM1 and ROOM2.
     * @param room1
     * @param room2
     * @param world
     * @param random
     */
    private static void connectRooms(Room room1, Room room2, TETile[][] world, Random random) {
        Hallway hallway = null;
        do {
            int posX1 = RandomUtils.uniform(random, room1.getX1() + 1, room1.getX2() - 1);
            int posY1 = RandomUtils.uniform(random, room1.getY1() + 1, room1.getY2() - 1);
            Point hallwayPoint1 = new Point(posX1, posY1);
            int posX2 = RandomUtils.uniform(random, room2.getX1() + 1, room2.getX2() - 1);
            int posY2 = RandomUtils.uniform(random, room2.getY1() + 1, room2.getY2() - 1);
            Point hallwayPoint2 = new Point(posX2, posY2);
            int hallwayWidth;
            if (isWide(random)) {
                hallwayWidth = 1;
            } else {
                hallwayWidth = 0;
            }
            hallway = new Hallway(hallwayPoint1, hallwayPoint2, hallwayWidth);
        } while (!hallway.isValid(world));
        hallway.generateHallway(world);
    }

    /**
     * Has a
     * @param random
     * @return
     */
    private static boolean isWide(Random random) {
        return RandomUtils.uniform(random, 0.0, 1.0) < WIDECHANCE;
    }
}
