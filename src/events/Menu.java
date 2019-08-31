package events;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;

import java.util.HashMap;

/*
this class allows for (only) the viewing of specific items in a list.

the displaying of an infinite amount of items is done by generating
navigating arrows (reactions) and proceeding buttons, labelled 1-5,
corresponding to embed text indicating each one's purpose.

clicking the arrows can either advance or return to different
parts of the menu, with the reaction buttons remaining the same--
corresponding to a specific row in the generated table.

todo: make this a lower-level class with extensions such as "VisualMenu" and specialized ones like "HelpMenu"?
 */
public class Menu extends ReactionListener {
    private final HashMap<Emote, Message> entries;



    Menu(HashMap<Emote, Message> entries) {
        this.entries = entries;
    }

    public boolean isCommand() {
        if (e.getUser().isBot()) return false;
        for (Emote x:entries.keySet())
            if (e.getReactionEmote().getEmote().equals(x))
                return true;
        return false;
    }

    public void onCommand() {

    }
}
