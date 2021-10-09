package byow.Core;



import java.awt.*;

import static byow.Core.Engine.*;
import static edu.princeton.cs.introcs.StdDraw.*;

public class Hud {

    private String tileString;
    private Color penColor;

    public Hud() {
        penColor = Color.WHITE;
    }

    public String getTileString() {
        return tileString;
    }

    public void setTileString(String s) {
        tileString = s;
    }

    public void render(GameRunner gameRunner) {
        int hudWidth = TRUEWIDTH - WIDTH;
        int hP = gameRunner.getPlayer().gethP();
        if (tileString != null) {
            setPenColor(penColor);
            Font font = new Font("Arial", Font.PLAIN, 30);
            setFont(font);
            text(WIDTH + hudWidth / 2, 2 * TRUEHEIGHT / 3, tileString);

            if (hP > 0) {
                text(WIDTH + hudWidth / 2, TRUEHEIGHT / 3, String.format("HP: %d", hP));
                if (gameRunner.isGameWon()) {
                    text(WIDTH + hudWidth / 2, TRUEHEIGHT / 8, "Congrats, you won.");
                }
                if (gameRunner.getFreezeDuration() > 0) {
                    text(WIDTH + hudWidth / 2, TRUEHEIGHT / 5, String.format("Shroomed for"
                            + " %d more turns.", gameRunner.getFreezeDuration()));
                } else if (gameRunner.getFreezeCoolDown() > 0) {
                    text(WIDTH + hudWidth / 2, TRUEHEIGHT / 5, String.format("Shroomify up in "
                            + "%d turns.", gameRunner.getFreezeCoolDown()));
                } else {
                    text(WIDTH + hudWidth / 2, TRUEHEIGHT / 5, String.format("F to Shroomify",
                            gameRunner.getFreezeCoolDown()));
                }
            } else {
                text(WIDTH + hudWidth / 2, TRUEHEIGHT / 3, "GAME OVER!");
            }
            show();
        }
    }

}
