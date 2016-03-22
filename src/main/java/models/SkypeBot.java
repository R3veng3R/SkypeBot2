package models;

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
import com.samczsun.skype4j.user.User;
import plugins.*;

import java.util.ArrayList;

public class SkypeBot {
    public static final String USERNAME = "marty.bot1";
    public static final String PASSWORD = "skin123cool";

    public static final String BOT_COMMAND_RU = "!бот";
    public static final String BOT_COMMAND_EN = "!bot";

    public static ArrayList<Plugin> plugins;

    public static void initBot() {
        try {
            init();

        } catch (Exception e) {
            System.err.println("Ошибка в SkypeBot");
            e.printStackTrace();
        }
    }

    private static void init() throws ConnectionException, NotParticipatingException, InvalidCredentialsException {
        Skype skype = new SkypeBuilder(USERNAME, PASSWORD).withResource("/v1/users/ME/conversations/ALL/messages").build();

        skype.getEventDispatcher().registerListener(new Listener() {
            @EventHandler
            public void onMessage(MessageReceivedEvent e) {
                Chat chat = e.getChat();
                String msg = e.getMessage().getContent().asPlaintext();
                User sender = e.getMessage().getSender();
                processRequest(chat, msg, sender);
            }
        });
        skype.login();
        skype.subscribe();
        skype.setVisibility(Visibility.ONLINE);
        loadPlugins();
        Util.loggerInfo("SkypeBot загружен!");
    }

    private static void loadPlugins() {
        plugins = new ArrayList<>();
        plugins.add(new BotAnswer());
        plugins.add(new SayPlugin());
        plugins.add(new ShowTitsPlugin());
        plugins.add(new SmiteCounterPicks());
        plugins.add(new BotGreet());
        plugins.add(new BotHelp());
    }

    private static void processRequest(Chat chat, String message, User sender) {
        if (SkypeBot.containsBotCommand(message)) {
            boolean isExec = false;

            for (Plugin plugin : plugins) {
                for (String command : plugin.getCommands()) {
                    if (message.toLowerCase().contains(command)) {
                        isExec = plugin.isExec(chat, message, sender);

                        if (isExec) return;
                    }
                }
            }

            try {
                chat.sendMessage("Извините, но такой команды я не знаю, для получения информации о всех командах напишите !бот помощь.");

            } catch (ConnectionException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean containsBotCommand(String msg) {
        return msg.toLowerCase().contains(BOT_COMMAND_RU) || msg.toLowerCase().contains(BOT_COMMAND_EN);
    }
}
