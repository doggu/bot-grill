package events;

import net.dv8tion.jda.core.entities.Emote;

public class Reactions extends ReactionListener {
    public boolean isCommand() {
        if (e.getReaction().getReactionEmote().isEmote()) {
            switch (e.getReaction().getReactionEmote().getEmote().getId()) {
                case "439339083215405067": //merthH
                    return true;
                default:
                    //System.out.println(e.getReaction().getReactionEmote().getEmote().getId());
            }
        }
        return false;
    }

    public void onCommand() {
        Emote baeda = e.getJDA().getEmotesByName("baeda", true).get(0);
        switch(e.getReaction().getReactionEmote().getEmote().getId()) {
            case "439339083215405067": //merthH
                addReaction(baeda);
                break;
        }
    }
}
