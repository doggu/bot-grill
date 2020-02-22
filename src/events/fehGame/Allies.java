package events.fehGame;

import discordUI.feh.BarracksWindow;
import discordUI.feh.FEHPrinter;
import feh.players.Barracks;
import feh.players.Summoner;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

public class Allies extends FEHCommand {
    public boolean isCommand() {
        return args[0].equalsIgnoreCase("getAllies"); }

    public void onCommand() {
        for (Summoner x:Summoner.SUMMONERS) {
            if (x.getUser().getId().equals(e.getAuthor().getId())) {
                Barracks barracks = x.getBarracks();
                if (args.length>1) {
                    try {
                        int allyIndex = Integer.parseInt(args[1]);
                        if (allyIndex>barracks.size()||allyIndex<0) {
                            sendMessage("could not find that unit. " +
                                    "please try again.");
                            log("user provided invalid index: "+allyIndex);
                            return;
                        }
                        sendMessage(new MessageBuilder(
                                FEHPrinter.printUnit(barracks.get(allyIndex-1)))
                                .build());
                        log("provided data on "+x.getName()+"\'s unit");
                        return;
                    } catch (NumberFormatException g) {
                        switch(args[1].toLowerCase()) {
                            case "atk":
                                barracks.sortByAtk();
                                break;
                            case "spd":
                            case "def":
                            case "res":
                            default:
                                //it's sorted by however it was before i guess
                        }
                    }
                }
                Message header = new MessageBuilder("You have ")
                        .append(barracks.size())
                        .append(" unit").append(barracks.size()==1?".":"s.")
                        .build();

                new BarracksWindow(x, e.getChannel(), header);
                return;
            }
        }

        Summoner.register(e.getAuthor().getId());
        onCommand();
    }



    public String getName() { return "Allies"; }
    public String getDescription() {
        return "A place to view your barracks—" +
                "all the allies you've ever summoned!"; }
    public String getFullDescription() {
        return  "Currently a work in progress: only allows a user to view " +
                        "a list of names of the units they have acquired.\n\n" +
                "In the future, this command will function similarly to " +
                "the real Allies section, providing team-building and other " +
                "ally-manipulation tools.";
    }
}
