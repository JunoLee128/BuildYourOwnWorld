package byow.Core;

public class Direction {

    private boolean UP = false;
    private boolean DOWN = false;
    private boolean LEFT = false;
    private boolean RIGHT = false;

    public Direction(String arg) {
        if (arg.equals("UP")) {
            UP = true;
            return;
        }
        if (arg.equals("DOWN")) {
            DOWN = true;
            return;
        }
        if (arg.equals("RIGHT")) {
            RIGHT = true;
            return;
        }
        if (arg.equals("LEFT")) {
            LEFT = true;
            return;
        } else {
            System.out.println("Input a valid direction!");
        }
    }

    //Transform a point based on the given direction and amount of tiles to move
    public Point transform(Point currentPoint, int amount) {
        if (UP) {
            return moveVertical(currentPoint, amount);
        }
        if (DOWN) {
            return moveVertical(currentPoint, -amount);
        }
        if (LEFT) {
            return moveHorizontal(currentPoint, -amount);
        }
        if (RIGHT) {
            return moveHorizontal(currentPoint, amount);
        } else {
            System.out.println("Setup Direction first!");
            return null;
        }
    }

    private Point moveVertical(Point currentPoint, int amount) {
        int posX = currentPoint.getX();
        int posY = currentPoint.getY() + amount;
        return new Point(posX, posY);
    }

    private Point moveHorizontal(Point currentPoint, int amount) {
        int posX = currentPoint.getX() + amount;
        int posY = currentPoint.getY();
        return new Point(posX, posY);
    }




}
