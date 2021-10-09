package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import static edu.princeton.cs.introcs.StdDraw.*;

public class Engine {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 60;
    public static final int TRUEWIDTH = WIDTH + 20; //game width plus hud
    public static final int TRUEHEIGHT = HEIGHT;
    public static final int FRAMERATE = 60;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        TERenderer ter = new TERenderer();
        ter.initialize(TRUEWIDTH, TRUEHEIGHT);
        ProgramRunner programRunner = new ProgramRunner(ter);
        programRunner.drawScreen();
        while (programRunner.getScreenState() != -1) {
            programRunner.drawScreen();
            while (hasNextKeyTyped()) {
                programRunner.handleKeyPress(nextKeyTyped());
            }
            pause(1000 / FRAMERATE);
        }
        System.exit(0);
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        ProgramRunner programRunner = new ProgramRunner(null);
        for (char c : input.toCharArray()) {
            programRunner.handleKeyPress(c);
            if (programRunner.getScreenState() == -1) {
                return programRunner.getWorld();
            }
        }
        return programRunner.getWorld();
    }
}
