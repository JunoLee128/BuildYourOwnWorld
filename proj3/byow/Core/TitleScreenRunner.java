package byow.Core;

import java.awt.*;

import static byow.Core.Engine.TRUEWIDTH;
import static byow.Core.Engine.TRUEHEIGHT;
import static edu.princeton.cs.introcs.StdDraw.*;

public class TitleScreenRunner {

    private ProgramRunner programRunner;
    private boolean enteringSeed;
    private long seed;
    private Color backgroundColor;
    private Color penColor;

    public TitleScreenRunner(ProgramRunner programRunner) {
        this.programRunner = programRunner;
        this.enteringSeed = false;
        this.backgroundColor = Color.DARK_GRAY;
        this.penColor = Color.WHITE;
        this.seed = -1;
    }

    private void pressN() {
        enteringSeed = true;
    }

    private void pressL() {
        programRunner.loadNewWorld(GameState.loadState());
    }

    private void pressQ() {
        programRunner.quit();
    }

    public void handleKeyPress(char c) {
        c = Character.toUpperCase(c);
        if (c == 'S' && seed != -1) {
            programRunner.startNewWorld(seed);
        }
        if (enteringSeed) {
            if (Character.isDigit(c)) {
                if (seed == -1) {
                    seed = Character.getNumericValue(c);
                } else {
                    seed *= 10;
                    seed += Character.getNumericValue(c);
                }
            }
        } else {
            switch (c) {
                case 'N':
                    pressN();
                    break;
                case 'L':
                    pressL();
                    break;
                case 'Q':
                    pressQ();
                    break;
                default:
                    break;
            }
        }
    }

    public void drawScreen() {
        if (enteringSeed) {
            clear(backgroundColor);
            setPenColor(penColor);
            Font font = new Font("Arial", Font.PLAIN, 30);
            setFont(font);
            text(TRUEWIDTH / 2, 2 * TRUEHEIGHT / 3, "Enter Seed:");
            font = new Font("Arial", Font.PLAIN, 20);
            if (seed != -1) {
                text(TRUEWIDTH / 2, TRUEHEIGHT / 3, String.valueOf(seed));
            }
            show();
        } else {
            clear(backgroundColor);
            setPenColor(penColor);
            Font font = new Font("Arial", Font.BOLD, 40);
            setFont(font);
            text(TRUEWIDTH / 2, 2 * TRUEHEIGHT / 3, "Wizard Runner");
            font = new Font("Arial", Font.PLAIN, 20);
            setFont(font);
            text(TRUEWIDTH / 2, TRUEHEIGHT / 3, "New Game (N)");
            text(TRUEWIDTH / 2, TRUEHEIGHT / 3 - 5, "Load Game (L)");
            text(TRUEWIDTH / 2, TRUEHEIGHT / 3 - 10, "Quit (Q)");
            show();
        }
    }
}
