package events.reactionary;

import events.MessageListener;
import net.dv8tion.jda.api.entities.Emote;

import java.util.List;

public class Vote extends MessageListener {
    public boolean isCommand() {
        return args[0].equalsIgnoreCase("vote");
    }

    public void onCommand() {
        List<Emote>
                heads_e = e.getJDA().getEmotesByName("heads", true),
                tails_e = e.getJDA().getEmotesByName("tails", true);

        if (heads_e.size()==0||tails_e.size()==0) {
            sendMessage("i'm having issues finding emotes. "+
                    "sorry! please try again later.");
            return;
        }

        Emote   heads = heads_e.get(0),
                tails = tails_e.get(0);

        e.getMessage().addReaction((Math.random()>0.5)?heads:tails).complete();
    }

    public char getPrefix() { return '%'; }
}
