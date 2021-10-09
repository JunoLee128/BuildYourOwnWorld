package byow.Core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import static byow.Core.RandomUtils.bernoulli;

public class Chunk implements Serializable {
    private final double biggestHeight = Engine.HEIGHT * 0.3;
    private final double biggestWidth = Engine.WIDTH * 0.3;
    private final double smallestHeight = Engine.HEIGHT * 0.15;
    private final double smallestWidth = Engine.WIDTH * 0.15;

    private int width;
    private int height;
    private Point base;
    private Chunk leftChunk;
    private Chunk rightChunk;
    private Room containedRoom;

    public Chunk(int w, int h, Point point) {
        width = w;
        height = h;
        base = point;
    }

    public void setRoom(Room room) {
        containedRoom = room;
    }

    public Room getRoom() {
        return containedRoom;
    }

    /**
     * Returns a random room contained in a subchunk (or this chunk, if no children).
     * @return
     */
    public Room getRandomRoom(Random random) {
        if (leftChunk != null && rightChunk != null) {
            if (bernoulli(random)) {
                return leftChunk.getRandomRoom(random);
            } else {
                return rightChunk.getRandomRoom(random);
            }
        } else {
            return getRoom();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Point getTopLeft() {
        int posX = base.getX();
        int posY = base.getY() + height;
        return new Point(posX, posY);
    }

    public Point getTopRight() {
        int posX = base.getX() + width;
        int posY = base.getY() + height;
        return new Point(posX, posY);
    }

    public Point getBottomRight() {
        int posX = base.getX() + width;
        int posY = base.getY();
        return new Point(posX, posY);
    }

    public Point getBottomLeft() {
        return base;
    }

    public void splitChunk(ArrayList<Chunk> roomChunkList, Random random) {
        int splitChooser = chunkSplitChooser(random);
        if (splitChooser == 0) {
            // Can't be split, will become roomChunk so add to list and return
            roomChunkList.add(this);
            return;
        }
        if (splitChooser == 1) {
            splitVertical(roomChunkList, random);
        }
        if (splitChooser == 2) {
            splitHorizontal(roomChunkList, random);
        }
    }

    /* Split the chunk vertically and create 2 new child chunks.
           -Both chunks should have height equal to the parent height and
                                   width equal to half the parent width
           -Left chunk base should be equal to parent chunk base
           -Right chunk base should be parent x + new chunk width, and parent y
       Call splitChunk on the children chunks
     */
    private void splitVertical(ArrayList<Chunk> roomChunkList, Random random) {
        int chunkHeight = height;
        int chunkWidth = width / 2;
        int rightX = base.getX() + chunkWidth;
        int rightY = base.getY();
        Point leftChunkBase = base;
        Point rightChunkBase = new Point(rightX, rightY);
        this.leftChunk = new Chunk(chunkWidth, chunkHeight, leftChunkBase);
        this.rightChunk = new Chunk(chunkWidth, chunkHeight, rightChunkBase);
        leftChunk.splitChunk(roomChunkList, random);
        rightChunk.splitChunk(roomChunkList, random);
    }

    /* Split the chunk horizontally and create 2 new child chunks.
           -Both chunks should have width equal to the parent width and
                                   height equal to half the parent height
           -Bottom chunk is left chunk, top chunk is right chunk
           -Left chunk base should be equal to parent chunk base
           -Right chunk base should be parent x and parent y + new chunk height
       Call splitChunk on the children chunks
     */
    private void splitHorizontal(ArrayList<Chunk> roomChunkList, Random random) {
        //Left = bottom, right = top
        int chunkWidth = width;
        int chunkHeight = height / 2;
        int rightX = base.getX();
        int rightY = base.getY() + chunkHeight;
        Point leftChunkBase = base;
        Point rightChunkBase = new Point(rightX, rightY);
        this.leftChunk = new Chunk(chunkWidth, chunkHeight, leftChunkBase);
        this.rightChunk = new Chunk(chunkWidth, chunkHeight, rightChunkBase);
        leftChunk.splitChunk(roomChunkList, random);
        rightChunk.splitChunk(roomChunkList, random);
    }

    /*Chooses if/how the chunk should split
      0 = dont split
      1 = split vertical
      2 = split horizontal
     */
    public int chunkSplitChooser(Random random) {

        if (height <= smallestHeight) {
            return 0;
        }
        if (width <= smallestWidth) {
            return 0;
        }
        if ((width / 2) < smallestWidth && (height / 2) < smallestHeight) {
            return 0;
        }
        //Check if we have a really wide room
        if (width < height * 0.40) {
            return 2;
        }
        if (height < width * 0.40) {
            return 1;
        }
        if (width <= biggestWidth && height <= biggestHeight) {
            return RandomUtils.uniform(random, 0, 3);
        }
        return RandomUtils.uniform(random, 1, 3);
    }

    public Chunk getLeftChunk() {
        return leftChunk;
    }

    public Chunk getRightChunk() {
        return rightChunk;
    }



}
