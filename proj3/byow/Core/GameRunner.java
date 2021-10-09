package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static byow.Core.CreateWorld.makeWorld;
import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;
import static edu.princeton.cs.introcs.StdDraw.*;

public class GameRunner {

    private ProgramRunner programRunner;
    private TETile[][] world;
    private Player player;
    private TERenderer ter;
    private Random random;
    private Hud hud;
    private Enemy enemy;
    private char lastChar;
    private List<Enemy> enemyList;
    private int touchedGoal;
    private ArrayList<Chunk> roomChunkList;
    private int freezeCoolDown;
    private int freezeDuration;
    private boolean doAnimation = true;

    public GameRunner(ProgramRunner programRunner, long seed, TERenderer ter) {
        this.programRunner = programRunner;
        random = new Random(seed);
        world = makeWorld(random);
        roomChunkList = RoomGeneration.getRoomChunkList();
        player = PlayerControl.spawnPlayer(roomChunkList, random, world, this, ter);
        player.generateEntity(world);
        this.enemyList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            enemy = EnemyControl.spawnEnemy(player, roomChunkList, random, world);
            enemy.generateEntity(world);
            enemyList.add(enemy);
        }
        RoomGeneration.spawnWinningTile(world, random);
        this.ter = ter;
        this.hud = new Hud();
        this.touchedGoal = 0;
        this.freezeCoolDown = 0;
        this.freezeDuration = 0;
    }

    /**
     * Load world.
     * @param gameState
     */
    public GameRunner(ProgramRunner programRunner, GameState gameState, TERenderer ter) {
        this.programRunner = programRunner;
        random = gameState.getrandom();
        world = gameState.getWorld();
        player = gameState.getPlayer();
        enemyList = gameState.getEnemyList();
        touchedGoal = gameState.getTouchedGoal();
        freezeDuration = gameState.getFreezeDuration();
        freezeCoolDown = gameState.getFreezeCooldown();
        this.ter = ter;
        this.hud = new Hud();
        this.roomChunkList = (ArrayList<Chunk>) gameState.getRoomChunkList();
    }

    public void handleTurn(Player player1, TETile[][] world1) {
        //other npcs take turn
        if (this.freezeCoolDown > 0) {
            this.freezeCoolDown -= 1;
        }
        if (this.freezeDuration > 0) {
            this.freezeDuration -= 1;
            for (Enemy troll : enemyList) {
                troll.makeShroom(world);
            }
            return;
        } else {
            for (Enemy troll : enemyList) {
                troll.revertShroom(world);
            }
        }
        ArrayList<Enemy> deadList = new ArrayList<>();
        for (Enemy troll : enemyList) {
            troll.takeTurn(player, world, ter, this);
            if (!troll.isAlive()) {
                deadList.add(troll);
            }
        }
        for (Enemy troll: deadList) {
            respawnEnemy(troll);
        }
    }

    public void touchedGoal() {
        if (isGameWon()) {
            gameWon();
        } else {
            touchedGoal += 1;
            RoomGeneration.spawnWinningTile(world, random);
        }

    }

    public void respawnEnemy(Enemy troll) {
        enemyList.remove(troll);
        Enemy newEnemy = EnemyControl.spawnEnemy(player, roomChunkList, random, world);
        newEnemy.generateEntity(world);
        enemyList.add(newEnemy);
    }

    public void gameOver() {
        System.out.println("YOU LOST!");
    }

    public void gameWon() {
        System.out.println("YOU WON!");
    }

    public void handleKeyPress(char c) {
        c = Character.toUpperCase(c);
        switch (c) {
            case 'W':
                if (player.gethP() > 0 && !isGameWon()) {
                    player.move(new Direction("UP"), 1, world, this);
                    handleTurn(player, world);
                }
                break;
            case 'A':
                if (player.gethP() > 0 && !isGameWon()) {
                    player.move(new Direction("LEFT"), 1, world, this);
                    handleTurn(player, world);
                }
                break;
            case 'S':
                if (player.gethP() > 0 && !isGameWon()) {
                    player.move(new Direction("DOWN"), 1, world, this);
                    handleTurn(player, world);
                }
                break;
            case 'D':
                if (player.gethP() > 0 && !isGameWon()) {
                    player.move(new Direction("RIGHT"), 1, world, this);
                    handleTurn(player, world);
                }
                break;
            case 'F':
                if (this.freezeCoolDown == 0 && player.gethP() > 0 && !isGameWon()) {
                    this.freezeDuration = 15;
                    this.freezeCoolDown = 30;
                    handleTurn(player, world);
                    System.out.println("FROZE ENEMIES");
                } else {
                    System.out.println("FREEZE ON COOLDOWN");
                }
                break;
            case 'P':
                for (Enemy x: enemyList) {
                    x.showPath(world, player, ter);
                }
                break;
            case 'Q':
                if (lastChar == ':') {
                    GameState gameState = new GameState(world, player, random, enemyList,
                            roomChunkList, touchedGoal, freezeDuration, freezeCoolDown);
                    gameState.saveState();
                    programRunner.setScreenState(-1);
                }
                break;
            case 'R':
                if (lastChar == ':') {
                    programRunner.setScreenState(0);
                    programRunner.reset();
                }
                break;
            case '.': //rest for a turn
                if (player.gethP() > 0 && !isGameWon()) {
                    handleTurn(player, world);
                }
                break;
            default:
                break;
        }
        lastChar = c;
    }

    public void updateRealtime() {
        Point mousePoint = new Point((int) mouseX(), (int) mouseY());
        if (mousePoint.getX() < WIDTH && mousePoint.getY() < HEIGHT) {
            hud.setTileString(mousePoint.getTile(world).description());
        }
    }

    public void drawScreen() {
        updateRealtime();
        ter.renderFrame(world);
        hud.render(this);
        //show();
    }

    public TETile[][] getWorld() {
        return world;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isGameWon() {
        return touchedGoal == 3;
    }

    public int getFreezeCoolDown() {
        return freezeCoolDown;
    }

    public int getFreezeDuration() {
        return freezeDuration;
    }

    public void setDoAnimation(boolean boo) {
        doAnimation = boo;
    }

    public boolean getDoAnimation() {
        return doAnimation;
    }
}
