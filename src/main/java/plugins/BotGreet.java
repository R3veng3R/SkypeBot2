package plugins;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.exceptions.SkypeException;
import com.samczsun.skype4j.user.User;

/**
 * Created by ALEX on 21.03.2016.
 */
public class BotGreet extends Plugin{

    public BotGreet() {
        this.pluginName = "Bot Greet";
        this.setCommands(new String[]{"привет", "хелло", "hi", "хай", "здорова"});

        this.pluginLoaded();
    }

    @Override
    public String getHelpMessage() {
        return "привет - поздароваться с ботом. Понимает англ. язык тоже. [ !бот привет ]";
    }

    @Override
    public boolean isExec(Chat chat, String message, User user) {
        try {
            sayHello(chat, user);
            return true;

        } catch (SkypeException e) {
            System.err.println("Exception in BotGreet !!!");
            e.printStackTrace();
        }

        return false;
    }

    private void sayHello(Chat chat, User sender) throws SkypeException {
        chat.sendMessage("О привет, " + sender.getDisplayName() + ".");
    }
}
