package byow.Core;

import byow.TileEngine.TETile;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Essentially a Lambda function wrapper.
 * eval() returns true if given TETile is allowed, false otherwise.
 */
public class TileEvaluator {

    private Collection<TETile> allowed;
    private boolean inverted = false; //set to true to invert behavior

    public TileEvaluator(TETile... args) {
        allowed = new ArrayList<TETile>();
        for (TETile tile : args) {
            allowed.add(tile);
        }
    }

    public TileEvaluator(boolean invert, TETile... args) {
        this.inverted = invert;
        allowed = new ArrayList<TETile>();
        for (TETile tile : args) {
            allowed.add(tile);
        }
    }

    public boolean eval(TETile tile) {
        if (allowed.contains(tile)) {
            return !inverted;
        }
        return inverted;
    }

}
