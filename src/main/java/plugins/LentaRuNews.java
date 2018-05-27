package plugins;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;
import models.Util;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ALEX on 23.03.2016.
 */
public class LentaRuNews extends  Plugin {
    private static final String NEWS_URL = "http://lenta.ru/rss/columns";
    private ArrayList<News> news;

    public LentaRuNews() {
        this.pluginName = "Lenta.ru news";
        this.setCommands(new String[]{"новости", "news"});
        this.news = new ArrayList<>();

        pluginLoaded();
    }

    @Override
    public String getHelpMessage() {
        return "новости | news - печатает новости.";
    }

    @Override
    public boolean isExec(Chat chat, String message, User user) {
        try {
            getNews();

            if(!news.isEmpty()) {
                News newsToSend = news.get(ThreadLocalRandom.current().nextInt(news.size()));
                chat.sendMessage(newsToSend.getTitle() + "\n\n" + newsToSend.getDescription());

            } else {
                chat.sendMessage("Новостей нет :( ");
            }

            news.clear();
            return true;
        } catch (Exception e) {
            Util.loggerError("Could not load news!");
        }

        return false;
    }

    private void getNews() throws ParserConfigurationException, SAXException, IOException {
        SAXParser parser =
                SAXParserFactory.newInstance().newSAXParser();

        DefaultHandler handler = new NewsParser();
        parser.parse(NEWS_URL, handler);
    }

    private class NewsParser extends DefaultHandler {
        boolean isDescription = false;
        boolean isTitle = false;
        boolean isNewItem = false;
        String description;
        String title;

        public void startElement(String uri, String localName,
                                 String qName, Attributes attributes) throws SAXException {

            if (qName.equals("item")) {
                isNewItem = true;
            }

            if (qName.equals("description") && isNewItem) {
                isDescription = true;
            }

            if(qName.equals("title") && isNewItem) {
                isTitle = true;
            }
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equals("item")) {
                news.add(new News(this.title, this.description));
                title = "";
                description = "";
                isNewItem = false;
            }

            if (qName.equals("description")) {
                isDescription = false;
            }

            if(qName.equals("title")) {
                isTitle = false;
            }
        }

        public void characters(char ch[], int start, int length) throws SAXException {
            if (isDescription) {
                description += new String(ch, start, length).trim();
                description = description.replaceAll("null", "");
            }

            if(isTitle) {
                title = new String(ch, start, length);
            }
        }
    }

    private class News {
        private String description;
        private String title;

        public News(String title, String description) {
            this.description = description;
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public String getTitle() {
            return title;
        }
    }

}
