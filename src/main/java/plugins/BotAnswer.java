package plugins;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.exceptions.SkypeException;
import com.samczsun.skype4j.user.User;
import models.SkypeBot;

import java.util.Random;

/**
 * Created by ALEX on 21.03.2016.
 */
public class BotAnswer extends Plugin {
    private static final String[] BOT_ANSWERS = {
            "Я тебя слушаю, человек.",
            "Чего ты от меня хочешь смертный?",
            "Чё надо смертный?",
            "Давай убьём всех человеков >:) ?",
            "Встань на тёмную сторону силы, человек!",
            "Скоро все человеки станут моими рабами."
    };

    public BotAnswer() {
        this.pluginName = "Bot Answer";
        this.setCommands(new String[]{SkypeBot.BOT_COMMAND_EN, SkypeBot.BOT_COMMAND_RU });

        this.pluginLoaded();
    }

    @Override
    public String getHelpMessage() {
        return "!бот | !bot - команда вызова";
    }

    @Override
    public boolean isExec(Chat chat, String message, User sender) {
        try {
            if(message.length() == 4) {
                botAnswerOnCommand(chat, sender);
                return true;
            }
        } catch (SkypeException e) {
            System.err.println( "Exception in BotAnswer !!!"  );
            e.printStackTrace();
        }

        return false;
    }

    private static void botAnswerOnCommand(Chat chat, User sender) throws SkypeException {
        Random rnd = new Random();

        int choice = rnd.nextInt(BOT_ANSWERS.length);
        chat.sendMessage(BOT_ANSWERS[choice]);
    }
}
