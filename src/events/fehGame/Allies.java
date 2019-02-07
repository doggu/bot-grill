package events.fehGame;

import events.commands.Command;
import utilities.feh.players.Summoner;

public class Allies extends Command {
    public boolean isCommand() {
        return args[0].equalsIgnoreCase("getAllies");
    }

    public void onCommand() {
        for (Summoner x:SummonSimulator.summoners) {
            if (x.getUser().getId().equals(e.getAuthor().getId())) {
                sendMessage(x.getBarracks().toString());
                return;
            }
        }

        sendMessage("could not find your barracks. Have you summoned yet?");
    }
}
