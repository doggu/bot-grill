package events.gameroom;

import events.MessageListener;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public abstract class Gameroom extends MessageListener {
    public boolean isCommand() {
        return e.getChannel().getName().equals("gameroom");
    }
    protected char getPrefix() { return '?'; }
}
