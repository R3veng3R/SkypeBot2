import models.SkypeBot;
import models.Util;
import org.apache.log4j.PropertyConfigurator;

/**
 * Created by ALEX on 18.03.2016.
 */
public class Main {

    public static void main(String[] args) {
        loadLogger();
        SkypeBot.initBot();
    }

    private static void loadLogger() {
        PropertyConfigurator.configure(Util.loadResource("/log4j/log4j.properties"));
        Util.loggerInfo("LOGGER SUCCESSFULLY INITIATED!");
    }
}
