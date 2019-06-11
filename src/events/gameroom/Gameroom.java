package events.gameroom;

import events.MessageListener;

public abstract class Gameroom extends MessageListener {
    public boolean isCommand() {
        return e.getChannel().getName().equals("gameroom");
    }
    protected char getPrefix() { return '?'; }
}