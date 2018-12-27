package events.commands;

import net.dv8tion.jda.core.entities.Emote;

import java.util.List;

public class Emotes extends Command {
    public void onCommand() {
        if (args[0].equals("getEmotes")) {
            List<Emote> emotes = e.getJDA().getEmotes();
            String list = "";
            for (int i = 0; i < emotes.size(); i++) {
                list += "<"+(emotes.get(i).isAnimated()?"a":"")+":" + emotes.get(i).getName() + ":" + emotes.get(i).getId() + ">";
                if ((i + 1) % 40 == 0) {
                    sendMessage(list);
                    list = "";
                }
            }

            if (list.length() > 0)
                sendMessage(list);

            log("listed emotes");
        } else if (args.length>1) {
            List<Emote> emote = e.getJDA().getEmotesByName(args[1],true);
            if (emote.size()==0) {
                sendMessage("could not find your emote.");
                log("could not find "+args[1]);
                return;
            }
            sendMessage("<"+(emote.get(0).isAnimated()?"a":"")+":"+emote.get(0).getName()+":"+emote.get(0).getId()+">");
            log("sent emote \""+args[1]+"\"");
        }
    }

    public boolean isCommand() {
        return args[0].contains("getEmote");
    }
}
