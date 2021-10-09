package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

public class ProgramRunner {

    private int screenState; //0 is titlescreen, 1 is game, -1 is quit
    private GameRunner gameRunner;
    private TitleScreenRunner titleScreenRunner;
    private TERenderer ter;

    public ProgramRunner(TERenderer ter) {
        this.screenState = 0;
        this.titleScreenRunner = new TitleScreenRunner(this);
        this.ter = ter;
    }

    public int getScreenState() {
        return screenState;
    }

    public void setScreenState(int stateNum) {
        this.screenState = stateNum;
    }

    public void handleKeyPress(char c) {
        if (screenState == 0) {
            titleScreenRunner.handleKeyPress(c);
        } else if (screenState == 1) {
            gameRunner.handleKeyPress(c);
        }
    }

    public void drawScreen() {
        if (this.screenState == 0) {
            titleScreenRunner.drawScreen();
        } else if (screenState == 1) {
            gameRunner.drawScreen();
        }
    }

    public void quit() {
        screenState = -1;
    }

    public void startNewWorld(long seed) {
        screenState = 1;
        gameRunner = new GameRunner(this, seed, ter);
    }

    public void loadNewWorld(GameState gameState) {
        if (gameState != null) {
            screenState = 1;
            gameRunner = new GameRunner(this, gameState, ter);
        } else {
            screenState = -1;
        }
    }

    public TETile[][] getWorld() {
        if (gameRunner != null) {
            return gameRunner.getWorld();
        }
        return null;
    }

    public void reset() {
        this.screenState = 0;
        this.titleScreenRunner = new TitleScreenRunner(this);
    }

}
