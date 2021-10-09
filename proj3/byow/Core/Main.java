package byow.Core;


/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byow.Core.Engine class take over
 *  in either keyboard or input string mode.
 */
public class Main {
    public static void main(String[] args) {

        /*
        Engine eng = new Engine();
        TETile[][] w1 = eng.interactWithInputString("N123S");
        TETile[][] w2 = eng.interactWithInputString("N123S");
        System.out.println(w1);
        System.out.println(w2);
        System.out.println(Arrays.deepEquals(w1,w2));*/

        if (args.length > 2) {
            System.out.println("Can only have two arguments - the flag and input string");
            System.exit(0);
        } else if (args.length == 2 && args[0].equals("-s")) {
            Engine engine = new Engine();
            engine.interactWithInputString(args[1]);
        // DO NOT CHANGE THESE LINES YET ;)
        } else if (args.length == 2 && args[0].equals("-p")) {
            System.out.println("Coming soon.");
        } else {
            Engine engine = new Engine();
            engine.interactWithKeyboard();
        }
        // DO NOT CHANGE THESE LINES YET ;)
    }
}
