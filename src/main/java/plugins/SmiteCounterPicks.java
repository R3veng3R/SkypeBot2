package plugins;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.formatting.Message;
import com.samczsun.skype4j.formatting.Text;
import com.samczsun.skype4j.user.User;
import models.Util;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * Created by ALEX on 21.03.2016.
 */
public class SmiteCounterPicks extends Plugin {
    private static final String MOBA_COUNTER_URL = "http://mobacounter.com";
    private static final int CONNECTION_TIMEOUT = 10000;

    public SmiteCounterPicks() {
        this.pluginName = "Smite Counter Picks";
        this.setCommands(new String[]{"smite", "смайт"});

        this.pluginLoaded();
    }

    @Override
    public String getHelpMessage() {
        return "смайт/smite (имя Бога на англ.) - показывает подробн. информацию об этом Боге. [ !бот smite agni ]";
    }

    @Override
    public boolean isExec(Chat chat, String message, User user) {
        try {
            getSmiteCounterPick(chat, message);
            return true;

        } catch (Exception e) {
            Util.loggerError("Exception in SmiteCounterPicks !!! ");
            try {
                chat.sendMessage("Не смог соединиться с сервером, возможно от не отвечает! Попробуйте позже.");
                return true;
            } catch (ConnectionException e1) {}
        }

        return false;
    }

    private void getSmiteCounterPick(Chat chat, String message) throws IOException, ConnectionException {
        String smiteCharacterUnformatted = message.substring(10).trim();
        String smiteCharacterLink = smiteCharacterUnformatted.replaceAll(" ", "-");

        Document doc = Jsoup.connect(MOBA_COUNTER_URL + "/smite/" + smiteCharacterLink).timeout(CONNECTION_TIMEOUT).get();
        String title = doc.title();

        if (!title.toLowerCase().contains(smiteCharacterUnformatted)) {
            chat.sendMessage("Персонажа " + smiteCharacterUnformatted + " не найдено!");
            return;
        }

        Elements counterBox = doc.getElementsByClass("counterBox");
        Elements weakAgainstCharacters = counterBox.get(0).getElementsByClass("counterChampion");
        Elements strongAgainstCharacters = counterBox.get(1).getElementsByClass("counterChampion");
        Elements goodStandingCharacters = counterBox.get(2).getElementsByClass("counterChampion");

        String weakAgainst = " ";
        if (!weakAgainstCharacters.isEmpty()) {
            for (Element character : weakAgainstCharacters) {
                weakAgainst += character.getElementsByTag("img").attr("alt") + ", ";
            }
        }

        String strongAgainst = " ";
        if (!strongAgainstCharacters.isEmpty()) {
            for (Element character : strongAgainstCharacters) {
                strongAgainst += character.getElementsByTag("img").attr("alt") + ", ";
            }
        }

        String goodWith = " ";
        if (!goodStandingCharacters.isEmpty()) {
            for (Element character : goodStandingCharacters) {
                goodWith += character.getElementsByTag("img").attr("alt") + ", ";
            }
        }

        Message messageToSend = getMessageToSend(
                smiteCharacterUnformatted,
                MOBA_COUNTER_URL + "/smite/" + smiteCharacterLink,
                weakAgainst,
                strongAgainst,
                goodWith
        );

        chat.sendMessage(messageToSend);
    }

    private String getCharacterFormattedName(String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    private Message getMessageToSend(String unformatedCharacterName, String link, String weakAgainst, String strongAgainst, String goodWith) {
        return Message.create()
                .with(Text.rich(getCharacterFormattedName(unformatedCharacterName)).withLink(link))
                .with(Text.NEW_LINE)
                .with(Text.rich("Слаб против:").withUnderline())
                .with(Text.plain(weakAgainst))
                .with(Text.NEW_LINE)
                .with(Text.NEW_LINE)
                .with(Text.rich("Силён против:").withUnderline())
                .with(Text.plain(strongAgainst))
                .with(Text.NEW_LINE)
                .with(Text.NEW_LINE)
                .with(Text.rich("Хорош в паре с:").withUnderline())
                .with(Text.plain(goodWith));
    }
}
