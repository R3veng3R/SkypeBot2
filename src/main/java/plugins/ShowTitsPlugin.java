package plugins;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.exceptions.SkypeException;
import com.samczsun.skype4j.user.User;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ALEX on 21.03.2016.
 */
public class ShowTitsPlugin extends Plugin {
    private static final String TITS_URL = "http://boobs-selfshots.tumblr.com/rss";
    private ArrayList<String> titsArray;

    public ShowTitsPlugin() {
        this.pluginName = "Tits";
        this.setCommands(new String[]{"tits", "boobs", "сиськи"});
        this.titsArray = new ArrayList<>();

        this.pluginLoaded();
    }

    @Override
    public String getHelpMessage() {
        return "сиськи. Да вроде и так понятно (18+)";
    }

    @Override
    public boolean isExec(Chat chat, String message, User user) {
        try {
            this.showTits(chat);
            return true;

        } catch (Exception e) {
            System.err.println( "Exception in TitsPlugin !!!"  );
            e.printStackTrace();
        }

        return false;
    }

    private void showTits(Chat chat) throws SkypeException, ParserConfigurationException, SAXException, IOException {
        Random rnd = new Random();

        SAXParser parser =
                SAXParserFactory.newInstance().newSAXParser();

        DefaultHandler handler = new TitsParser();
        parser.parse(TITS_URL, handler);

        if (!titsArray.isEmpty()) {
            int index = rnd.nextInt(titsArray.size());
            chat.sendMessage(titsArray.get(index));
        }

        titsArray.clear();
    }

    private class TitsParser extends DefaultHandler {
        boolean isDescription = false;
        boolean isNewItem = false;
        String description;

        public void startElement(String uri, String localName,
                                 String qName, Attributes attributes) throws SAXException {

            if (qName.equals("item")) {
                isNewItem = true;
            }

            if (qName.equals("description") && isNewItem) {
                isDescription = true;
            }
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equals("item")) {
                isNewItem = false;
                isDescription = false;
            }
        }

        public void characters(char ch[], int start, int length) throws SAXException {
            if (isDescription) {
                description = new String(ch, start, length);

                if (hasExtension(description)) {
                    description = description.substring(9, length - 2);
                    description = fixJPGExtension(description);
                    titsArray.add(description);
                }
            }
        }

        private String fixJPGExtension(String description) {
            if(description.charAt(description.length() - 1) == '.') {
                description += "jpg";

            } else if(description.charAt(description.length() - 1) == 'j') {
                description += "pg";

            } else if(description.charAt(description.length() - 1) == 'p') {
                description += "g";
            }

            return description;
        }

        private boolean hasExtension(String description) {
            return description.contains("img") && (description.contains("jpg") || description.contains("png"));
        }
    }
}
