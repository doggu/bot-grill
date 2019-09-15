package events.reactionary;

import events.ReactionListener;
import net.dv8tion.jda.core.entities.Emote;

public class Reactions extends ReactionListener {
    public boolean isCommand() {
        if (e.getReaction().getReactionEmote().isEmote())
            return e.getReaction().getReactionEmote().getEmote().getId().equals("439339083215405067");
        return false;
    }

    public void onCommand() {
        Emote baeda = e.getJDA().getEmotesByName("baeda", true).get(0);
        addReaction(baeda);
    }
}
