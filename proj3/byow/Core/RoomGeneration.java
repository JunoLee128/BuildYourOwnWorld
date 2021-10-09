package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

import static byow.Core.Engine.WIDTH;
import static byow.Core.Engine.HEIGHT;

public class RoomGeneration {

    private static ArrayList<Chunk> roomChunkList;

    //Check if the room is in bounds
    public static boolean isInBounds(Room room) {
        return room.getX1() >= 0 && room.getY1() >= 0
                && room.getX2() < WIDTH && room.getY2() < HEIGHT;
    }

    //Make "root" chunk, split that into roomsized chunks and call createChunkRoomsMethod
    public static void chunkGeneration(TETile[][] world, Random random) {
        roomChunkList = new ArrayList<>();
        Point base = new Point(0, 0);
        Chunk mapChunk = new Chunk(WIDTH - 1, HEIGHT - 1, base);
        mapChunk.splitChunk(roomChunkList, random);
        createChunkRooms(roomChunkList, world, random);
        HallwayGeneration.generateHallwaysNew(mapChunk, world, random);
    }

    public static ArrayList<Chunk> getRoomChunkList() {
        return roomChunkList;
    }

    /*For each room sized chunk in the roomChunkList, create a room inside it.
      The room can be any size between the set minSize and the size of the room chunk - 2
      We subtract 2 to keep room from touching borders of chunk, prevent rooms from touching
      Set the "room" variable in each chunk sized room to be the newly created room
      Call the generate Hallway method.
     */
    public static void createChunkRooms(ArrayList<Chunk> roomChunkList1,
                                        TETile[][] world, Random random) {
        for (Chunk chunk: roomChunkList1) {
            int minSize = Room.MINSIZE;
            int height = RandomUtils.uniform(random, minSize, chunk.getHeight() - 2);
            int width = RandomUtils.uniform(random, minSize, chunk.getWidth() - 2);
            int closestXPos = chunk.getTopRight().getX() - width;
            int closestYPos = chunk.getTopRight().getY() - height;
            int farthestXPos = chunk.getBottomLeft().getX() + 1;
            int farthestYPos = chunk.getBottomLeft().getY() + 1;
            int posX = RandomUtils.uniform(random, farthestXPos, closestXPos);
            int posY = RandomUtils.uniform(random, farthestYPos, closestYPos);
            Point roomPoint = new Point(posX, posY);
            Room room = new Room(roomPoint, width, height);
            room.generateRoom(world);
            chunk.setRoom(room);
        }
        //HallwayGeneration.generateHallway(roomChunkList, world, random);
    }

    //Print the base of each room sized chunk
    public static void printChunkRoomBases(ArrayList<Chunk> roomChunkList1) {
        for (Chunk chunk: roomChunkList1) {
            Point base = chunk.getBottomLeft();
            System.out.println("(" + base.getX() + ", " + base.getY() + ")");
        }
    }

    //Creates rooms the size of each room sized chunk, to visually see chunks.
    public static void outlineChunkRooms(ArrayList<Chunk> roomChunkList1, TETile[][] world) {
        for (Chunk chunk: roomChunkList1) {
            int roomX = chunk.getBottomLeft().getX();
            int roomY = chunk.getBottomLeft().getY();
            Point roomBase = new Point(roomX, roomY);
            int maxRoomHeight = chunk.getHeight();
            int maxRoomWidth = chunk.getWidth();
            //Temp, make room the max size of chunk
            Room room = new Room(roomBase, maxRoomWidth, maxRoomHeight);
            room.generateRoom(world);
        }
    }

    public static void spawnWinningTile(TETile[][] world, Random random) {
        int chosenChunkIndex = RandomUtils.uniform(random, 0, roomChunkList.size());
        Chunk chosenChunk = roomChunkList.get(chosenChunkIndex);
        Room chosenRoom = chosenChunk.getRoom();
        Point bottomLeft = new Point(chosenRoom.getX1() + 1, chosenRoom.getY1() + 1);
        Point bottomRight = new Point(chosenRoom.getX2() - 1, chosenRoom.getY1() + 1);
        Point topLeft = new Point(chosenRoom.getX1() + 1, chosenRoom.getY2() - 1);
        Point topRight = new Point(chosenRoom.getX2() - 1, chosenRoom.getY2() - 1);
        ArrayList<Point> iterateList = new ArrayList<>();
        iterateList.add(bottomLeft);
        iterateList.add(bottomRight);
        iterateList.add(topLeft);
        iterateList.add(topRight);
        for (int i = 0; i < 4; i++) {
            int randomIndex = RandomUtils.uniform(random, 0, iterateList.size());
            Point chosenPoint = iterateList.get(randomIndex);
            int posX = chosenPoint.getX();
            int posY = chosenPoint.getY();
            if (world[posX][posY] == Tileset.FLOOR) {
                world[posX][posY] = Tileset.GOAL;
                return;
            } else {
                iterateList.remove(randomIndex);
            }
        }
        //If we get out of the for loop, it means all 4 points were unavailable,
        // so we try a new random room
        spawnWinningTile(world, random);
    }
}



