package events.fehGame;

import events.commands.Command;

public abstract class FEHCommand extends Command {
    //TODO: implement FEH-specific features and integrate into FEH-related listeners

    public boolean isCommand() {
        //is BotMain.FEHEROES_UTILS true?
        return false;
    }
}
