package events.gameroom;

import events.MessageListener;
import net.dv8tion.jda.core.entities.Emote;

import java.util.List;

public class Vote extends MessageListener {
    public boolean isCommand() {
        return args[0].equalsIgnoreCase("vote");
    }

    public void onCommand() {
        List<Emote> emotes = e.getJDA().getEmotes();
        Emote heads = null, tails = null;

        for (Emote x:emotes) {
            if (x.getName().equals("heads")) {
                heads = x;
            }
            if (x.getName().equals("tails")) {
                tails = x;
            }

            if (heads!=null&&tails!=null) {
                break;
            }
        }

        e.getMessage().addReaction((Math.random()>0.5)?heads:tails).complete();
    }

    public char getPrefix() { return '%'; }
}
