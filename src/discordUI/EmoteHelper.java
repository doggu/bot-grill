package discordUI;

import main.BotMain;
import net.dv8tion.jda.core.entities.Emote;

public class EmoteHelper {
    public static Emote getEmote(String name) {
        try {
            return BotMain.bot_grill.getEmotesByName(name, true).get(0);
        } catch (IndexOutOfBoundsException ioobe) {
            return BotMain.bot_grill.getEmotes()
                    .get((int)(Math.random()*BotMain.bot_grill.getEmotes().size()));
        }
    }
    //todo: put this in some lower level class

    public static String printEmote(Emote e) {
        return "<:"+e.getName()+":"+e.getId()+">";
    }
}
