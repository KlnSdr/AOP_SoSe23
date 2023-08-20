package Sinking;

import Sinking.common.Json;

import java.util.ArrayList;
import java.util.Arrays;

public class ArgsLoader {
    public static ArrayList<String> twoPartArgs = new ArrayList<>(Arrays.asList("port", "p"));
    public static ArrayList<String> singlePartArgs = new ArrayList<>(Arrays.asList("server", "s", "test", "t"));

    public static Json load(String[] args) {
        Json json = new Json();
        // Parsing command line arguments
        for (int i = 0; i < args.length; i++) {
            int dashCount = args[i].length() - args[i].replace("-", "").length();
            String arg = args[i].replace("-", "");

            if (twoPartArgs.contains(arg) && i + 1 < args.length) {
                if (dashCount < 2) {
                    // assume that the full arg is the previous entry in the arraylist
                    arg = twoPartArgs.get(twoPartArgs.indexOf(arg) - 1);
                }
                json.set(arg, args[i + 1]);
                i++;
            } else if (singlePartArgs.contains(arg)) {
                if (dashCount < 2) {
                    // assume that the full arg is the previous entry in the arraylist
                    arg = singlePartArgs.get(singlePartArgs.indexOf(arg) - 1);
                }
                json.set(arg, "true");
            }
        }
        return json;
    }
}
