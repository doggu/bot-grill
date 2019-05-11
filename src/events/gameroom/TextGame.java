package events.gameroom;

import events.MessageListener;

public abstract class TextGame extends MessageListener {
    public abstract boolean isCommand();
    public abstract void onCommand();

    //TODO: may want to change this later (and rework this whole thing entirely)
    public char getPrefix() { return '?'; }
}
