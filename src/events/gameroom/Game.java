package events.gameroom;

import events.commands.Command;

public abstract class Game extends Command {
    public int minPlayers;
    public abstract String getName();
    public abstract int getMinPlayers();
    public abstract int getMaxPlayers();
}
