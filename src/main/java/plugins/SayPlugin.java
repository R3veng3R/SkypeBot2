package plugins;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.exceptions.SkypeException;
import com.samczsun.skype4j.user.User;

/**
 * Created by ALEX on 21.03.2016.
 */
public class SayPlugin extends Plugin {

    public SayPlugin() {
        this.pluginName = "Say";
        this.setCommands(new String[]{"say", "скажи"});

        this.pluginLoaded();
    }

    @Override
    public String getHelpMessage() {
        return "скажи - повторяет фразу [ !бот скажи привет ]";
    }

    @Override
    public boolean isExec(Chat chat, String message, User user) {
        try {
            say(chat, message, user);
            return true;

        } catch (SkypeException e) {
            System.err.println( "Exception in SayPlugin !!!"  );
            e.printStackTrace();
        }

        return false;
    }

    private void say(Chat chat, String msg, User sender) throws SkypeException {
        String msgToSay = msg.substring(10).trim();

        if (msgToSay.charAt(0) == 'я' && msgToSay.length() >= 2)
            msgToSay = msgToSay.replaceFirst("я", sender.getDisplayName());

        chat.sendMessage(msgToSay);
    }
}
