package events.fehGame;

import events.commands.Command;
import main.BotMain;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class FEHCommand extends Command {
    //TODO: implement FEH-specific features
    // and integrate into FEH-related listeners

    //todo: i genuinely don't think i have a graceful way of doing this;
    // solutions require modifying subclass implementation contracts or
    // duplicating what you see down below (from MessageListener)
    public void onMessageReceived(MessageReceivedEvent e) {
        String message = e.getMessage().getContentRaw();
        if (e.getAuthor().isBot()) return;
        if (message.length()==0) return;
        if (message.charAt(0)!=getPrefix()) return;
        this.e = e;
        this.args = message.substring(1).split(" ");
        if (isCommand()) {
            if (!BotMain.FEHEROES_UTILS) {
                sendMessage("sorry, FEH utilities are not " +
                        "available at this time. please try again later!");
                return;
            }
            e.getChannel().sendTyping().complete();
            onCommand();
        }
    }
}
