package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

//A* pathfinding implementation
public class PathFinding {

    public static ArrayList<Point> findPath(Point start, Point goal,
                                            TETile[][] world, Player player) {
        PriorityQueue<PathNode> openNodes = new PriorityQueue<>();
        HashMap<Point, PathNode> openNodePoints = new HashMap<>();
        ArrayList<PathNode> closedNodes = new ArrayList<>();
        HashMap<Point, PathNode> closedNodePoints = new HashMap<>();
        PathNode startNode = new PathNode(start);
        ArrayList<Point> changedFloor = new ArrayList<>();
        ArrayList<Point> changedEnemies = new ArrayList<>();
        ArrayList<Point> changedGoal = new ArrayList<>();
        startNode.setParent(null);
        startNode.setGCost(0);
        startNode.setHCost(0);
        startNode.setFCost(0);
        openNodes.add(startNode);
        while (!openNodes.isEmpty()) {
            PathNode current = openNodes.poll();
            closedNodes.add(current);
            ArrayList<PathNode> neighbors = getNeighbors(current, world);
            for (PathNode neighbor : neighbors) {
                int posX = neighbor.getPoint().getX();
                int posY = neighbor.getPoint().getY();
                TETile tile = world[posX][posY];
                if (tile.equals(Tileset.PLAYER)) {
                    return makePath(current, start, world,
                            changedFloor, changedEnemies, changedGoal);
                }
                if (!traversable(tile) || closedNodes.contains(neighbor)) {
                    continue;
                }
                neighbor.setGCost(getTentativeGCost(current, neighbor));
                neighbor.setHCost(manhattenDistance(neighbor.getPoint(), goal));
                neighbor.setFCost();
                if (openNodePoints.containsKey(neighbor.getPoint())
                        && openNodePoints.get(
                                neighbor.getPoint()).getFCost() < neighbor.getFCost()) {
                    continue;
                }
                if (closedNodePoints.containsKey(neighbor.getPoint())
                        && closedNodePoints.get(
                                neighbor.getPoint()).getFCost() < neighbor.getFCost()) {
                    continue;
                }
                openNodes.add(neighbor);
                openNodePoints.put(neighbor.getPoint(), neighbor);
                neighbor.setParent(current);
                if (tile.equals(Tileset.TROLL)) {
                    changedEnemies.add(new Point(posX, posY));
                }
                if (tile.equals(Tileset.GOAL)) {
                    changedGoal.add(new Point(posX, posY));
                }
                if (tile.equals(Tileset.FLOOR)) {
                    changedFloor.add(new Point(posX, posY));
                }
                world[posX][posY] = Tileset.SEARCHED_FLOOR;
            }
            closedNodes.add(current);
            closedNodePoints.put(current.getPoint(), current);

        }
        System.out.println("IF GET THIS, A* MESSED UP");
        ArrayList<Point> returnList = new ArrayList<>();
        returnList.add(start);
        return returnList;
    }

    private static int manhattenDistance(Point start, Point end) {
        return Math.abs(start.getX() - end.getX())
                + Math.abs(start.getY() - end.getY());
    }

    private static boolean traversable(TETile tile) {
        if (tile.equals(Tileset.FLOOR)) {
            return true;
        }
        if (tile.equals(Tileset.PLAYER)) {
            return true;
        }
        if (tile.equals(Tileset.GRASS)) {
            return true;
        }
        if (tile.equals(Tileset.TROLL)) {
            return true;
        }
        if (tile.equals(Tileset.GOAL)) {
            return true;
        }
        return false;
    }
    private static ArrayList<Point> makePath(PathNode node, Point start,
             TETile[][] world, ArrayList<Point> changedTiles, ArrayList<Point> changedEnemies,
                                             ArrayList<Point> changedGoal) {
        ArrayList<Point> pathList = new ArrayList<>();
        while (node != null) {
            pathList.add(node.getPoint());
            node = node.getParent();
        }
        for (Point point : changedTiles) {
            world[point.getX()][point.getY()] = Tileset.FLOOR;
        }
        for (Point point : changedEnemies) {
            world[point.getX()][point.getY()] = Tileset.TROLL;
        }
        for (Point point : changedGoal) {
            world[point.getX()][point.getY()] = Tileset.GOAL;
        }
        Collections.reverse(pathList);
        return pathList;
    }

    private static ArrayList<PathNode> getNeighbors(PathNode node, TETile[][] world) {
        ArrayList<PathNode> neighborList = new ArrayList<>();
        for (int y = -1; y < 2; y++) {
            for (int x = -1; x < 2; x++) {
                if (x == 0 && y == 0) {
                    continue;
                }
                Point newPoint = new Point(node.getPoint().getX() + x, node.getPoint().getY() + y);
                if (world[newPoint.getX()][newPoint.getY()].equals(Tileset.WALL)
                        || world[newPoint.getX()][newPoint.getY()].equals(Tileset.NOTHING)) {
                    continue;
                }
                PathNode newNode = new PathNode(newPoint);
                neighborList.add(newNode);
            }
        }
        return neighborList;

    }

    private static int getTentativeGCost(PathNode current, PathNode node) {
        int posX = current.getPoint().getX() - node.getPoint().getX();
        int posY = current.getPoint().getY() - node.getPoint().getY();
        if (!(posX == 0) && !(posY == 0)) {
            return current.getGCost() + 14;
        } else {
            return current.getGCost() + 10;
        }
    }
}
