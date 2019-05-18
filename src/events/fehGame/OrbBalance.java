package events.fehGame;

import events.commands.Command;
import events.fehGame.summoning.SummonSimulator;
import utilities.feh.players.Summoner;

public class OrbBalance extends Command {
    @Override
    public void onCommand() {
        Summoner match = null;
        for (Summoner x: SummonSimulator.summoners) {
            if (x.getUser().getId().equals(e.getAuthor().getId())) {  //technically unnecessary code
                match = x;
                break;
            }
        }
        if (match==null) {
            sendMessage("you have not spent any orbs yet!");
        } else {
            sendMessage("you have spent " + match.getOrbsSpent() +
                    " orb" + (match.getOrbsSpent() > 1 || match.getOrbsSpent() == 0 ? "s" : "") + ".");
            log("retrieved " + match.getName() + "'s orbs spent.");
        }
    }

    @Override
    public boolean isCommand() {
        return args[0].equalsIgnoreCase("getOrbsSpent");
    }



    public String getName() { return "OrbBalance"; }
    public String getDescription() { return "Find out how many orbs you've spent! (WIP)"; }
    public String getFullDescription() {
        return getDescription()+"\n" +
                "\tSyntax: \"?GetOrbsSpent\"\n" +
                "Returns the amount of orbs you've spent in the summoning simulator.\n" +
                "WIP, currently does not hold a true balance; only expenses.";
    }
}
