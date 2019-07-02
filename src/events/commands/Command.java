package events.commands;

import events.MessageListener;

//wtf did i even make this for
//TODO: make something other than this for more diverse situations
// this shit is dominating pretty much every listener
public abstract class Command extends MessageListener {
    public abstract boolean isCommand();
    protected char getPrefix() { return '?'; }

    public abstract String getName();
    public abstract String getDescription();
    public abstract String getFullDescription();
    public String getHelp() { return getName()+": "+getDescription(); }
    public String getFullHelp() { return "**"+getName()+"**\n"+getFullDescription(); }
}
