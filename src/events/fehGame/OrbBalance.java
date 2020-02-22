package events.fehGame;

import feh.players.Summoner;

public class OrbBalance extends FEHCommand {
    public void onCommand() {
        Summoner match = null;
        for (Summoner x : Summoner.SUMMONERS) {
                //technically unnecessary code
            if (x.getUser().getId().equals(e.getAuthor().getId())) {
                match = x;
                break;
            }
        }
        if (match==null) {
            sendMessage("you have not spent any orbs yet!");
        } else {
            sendMessage("you have spent " + match.getOrbsSpent() +
                    " orb" +
                    (match.getOrbsSpent() > 1 ||
                            match.getOrbsSpent() == 0 ? "s" : "") +
                    ".");
            log("retrieved " + match.getName() + "'s orbs spent.");
        }
    }

    public boolean isCommand() {
        return args[0].equalsIgnoreCase("getOrbsSpent");
    }


    public String getName() { return "OrbBalance"; }
    public String getDescription() {
        return "Find out how many orbs you've spent! (WIP)"; }
    public String getFullDescription() {
        return getDescription()+"\n" +
                "\tSyntax: \"?GetOrbsSpent\"\n" +
                "Returns the amount of orbs you've spent " +
                        "in the summoning simulator.\n" +
                "WIP, currently does not hold a true balance, only expenses.";
    }
}
