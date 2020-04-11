package discordUI;

import main.BotMain;
import net.dv8tion.jda.api.entities.Emote;

public class EmoteHelper {
    public static Emote getEmote(String name) {
        try {
            return BotMain.bot_grill.getEmotesByName(name, true)
                    .get(0);
        } catch (IndexOutOfBoundsException ioobe) {
            int r = (int)(Math.random()*BotMain.bot_grill.getEmotes().size());
            return BotMain.bot_grill.getEmotes()
                    .get(r);
        }
    }
    //todo: put this in some lower level class

    public static String printEmote(Emote e) {
        return "<:"+e.getName()+":"+e.getId()+">";
    }
}
