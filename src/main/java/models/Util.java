package models;


import org.apache.log4j.Logger;

import java.net.URL;

/**
 * Created by ALEX on 22.02.2016.
 */
public class Util {
    public static URL loadResource(String pathToFile) {
        return Util.class.getResource(pathToFile);
    }

    public static void loggerInfo(String message) {
        Logger.getLogger(Util.class).info(message);
    }

    public static void loggerWarn(String message) {
        Logger.getLogger(Util.class).warn(message);
    }

    public static void loggerError(String message) {
        Logger.getLogger(Util.class).error(message);
    }

    public static void exitWithError(String errorMsg) {
        loggerError(errorMsg);
        System.exit(1);
    }
}
