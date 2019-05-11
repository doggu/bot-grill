package events.commands;

import net.dv8tion.jda.core.entities.Emote;

import java.util.List;

public class Emotes extends Command {
    public void onCommand() {
        if (args[0].equals("getEmotes")) {
            List<Emote> emotes = e.getJDA().getEmotes();
            StringBuilder list = new StringBuilder();
            for (int i = 0; i < emotes.size(); i++) {
                list.append("<")
                        .append(emotes.get(i).isAnimated()?"a":"")
                        .append(":")
                        .append(emotes.get(i).getName())
                        .append(":")
                        .append(emotes.get(i).getId())
                        .append(">");
                if ((i + 1) % 40 == 0) {
                    sendMessage(list.toString());
                    list = new StringBuilder();
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



    public String getName() { return "getEmote"; }
    public String getDescription() { return "a tool for retrieving any emoticon i can access."; }
    public String getFullDescription() {
        return getDescription()+"\n" +
                "\tSyntax: \"?getEmote [name of emoticon]\"" +
                "I have many hidden servers dedicated to myself! " +
                "Most of them can be found through the \"Girl\" command.";
    }
}
