package events.commands;

import events.MessageListener;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

//wtf did i even make this for
//TODO: make something other than this for more diverse situations
// this shit is dominating pretty much every listener
public abstract class Command extends MessageListener {
    public abstract boolean isCommand();
    protected char getPrefix() { return '?'; }
}
