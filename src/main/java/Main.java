import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.SkypeBuilder;
import com.samczsun.skype4j.Visibility;
import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.events.EventHandler;
import com.samczsun.skype4j.events.Listener;
import com.samczsun.skype4j.events.chat.message.MessageReceivedEvent;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.exceptions.InvalidCredentialsException;
import com.samczsun.skype4j.exceptions.NotParticipatingException;
import com.samczsun.skype4j.exceptions.SkypeException;
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
