package javabot.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.rickyclarkson.java.util.TypeSafeList;
import javabot.BotEvent;
import javabot.Database;
import javabot.Message;
import javabot.Factoid;

/**
 * @author ricky_clarkson
 */
public class KarmaChangeOperation implements BotOperation {
    private final Database database;

    public KarmaChangeOperation(final Database factoidDatabase) {
        database = factoidDatabase;
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList< Message>();
        String message = event.getMessage();
        String sender = event.getSender();
        String channel = event.getChannel();
        if(message.indexOf(" ") != -1) {
            return messages;
        }
        if(message.endsWith("++") || message.endsWith("--")) {
            String nick = message.substring(0, message.length() - 2);
            nick = nick.toLowerCase();
            if(nick.equals(sender.toLowerCase())) {
                messages.add(new Message(channel,"Changing one's own karma" +
                    " is not permitted.", false));
                return messages;
            }
            int karma;
            Factoid factoid = database.getFactoid("karma " + nick);
            try {
                karma = Integer.parseInt(factoid.getValue());
            } catch(Exception exception) {
                karma = 0;
            }
            if(message.endsWith("++")) {
                karma++;
            } else {
                karma--;
            }
            factoid.setValue("" + karma, sender);
            database.updateFactoid(factoid);
            KarmaReadOperation karmaRead = new KarmaReadOperation(database);
            messages.addAll(karmaRead.handleMessage(new BotEvent(event.getChannel(),
                event.getSender(),event.getLogin(),event.getHostname(),
                "karma " + nick)));
        }
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList< Message>();
    }
}
