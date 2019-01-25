package events.commands;

import events.MessageListener;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

//wtf did i even make this for
public abstract class Command extends MessageListener {
    public abstract boolean isCommand();
    protected char getPrefix() { return '?'; }
}
