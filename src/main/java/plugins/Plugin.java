package plugins;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;
import models.Util;

/**
 * Created by ALEX on 21.03.2016.
 */
public abstract class Plugin {
    protected String[] commands;
    protected String pluginName;

    public String getPluginName() {
        return pluginName;
    }

    public String[] getCommands() {
        return commands;
    }

    public void setCommands(String[] commands) {
        this.commands = commands;
    }

    protected void pluginLoaded() {
        Util.loggerInfo("Плагин: " + getPluginName() + " успешно загружен!");
    }

    public abstract String getHelpMessage();
    public abstract boolean isExec(Chat chat, String message, User user);
}
