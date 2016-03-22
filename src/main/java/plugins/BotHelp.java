package plugins;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.exceptions.SkypeException;
import com.samczsun.skype4j.user.User;
import models.SkypeBot;

/**
 * Created by ALEX on 21.03.2016.
 */
public class BotHelp extends Plugin {

    public BotHelp() {
        this.pluginName = "Bot Help";
        this.setCommands(new String[]{"help", "помощь"});

        this.pluginLoaded();
    }

    @Override
    public String getHelpMessage() {
        return "помощь | help - вызывает эту подсказку.";
    }

    @Override
    public boolean isExec(Chat chat, String message, User user) {
        try {
            printHelp(chat);
            return true;

        } catch (SkypeException e) {
            System.err.println("Exception in BotHelp !!!");
            e.printStackTrace();
        }

        return false;
    }

    public void printHelp(Chat chat) throws SkypeException {
        String stars = "*********************************\n";

        String helpMessage = "";
        int count = 1;
        for(Plugin plugin: SkypeBot.plugins) {
            helpMessage += count + ". " + plugin.getHelpMessage() + "\n";
            count++;
        }

        chat.sendMessage(stars + helpMessage + stars);
    }
}
