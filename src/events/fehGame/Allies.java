package events.fehGame;

import discordUI.feh.FEHPrinter;
import discordUI.menu.Menu;
import discordUI.menu.MenuEntry;
import events.fehGame.summoning.SummonSimulator;
import feh.heroes.unit.Unit;
import feh.players.Barracks;
import feh.players.Summoner;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.util.ArrayList;

public class Allies extends FEHCommand {
    public boolean isCommand() {
        return args[0].equalsIgnoreCase("getAllies");
    }

    public void onCommand() {
        for (Summoner x: SummonSimulator.summoners) {
            if (x.getUser().getId().equals(e.getAuthor().getId())) {
                Barracks barracks = x.getBarracks();
                if (args.length>1) {
                    try {
                        int allyIndex = Integer.parseInt(args[1]);
                        if (allyIndex>barracks.size()||allyIndex<0) {
                            sendMessage("could not find that unit. please try again.");
                            log("user provided invalid index: "+allyIndex);
                            return;
                        }
                        sendMessage(
                                new MessageBuilder(
                                        FEHPrinter.printUnit(
                                                barracks.get(allyIndex-1),
                                                false)).build());
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
                        .append(" unit").append(barracks.size()==1?"":"s").append(".")
                        .build();

                ArrayList<MenuEntry> entries = new ArrayList<>();

                for (Unit unit:barracks)
                    entries.add(new MenuEntry(unit.toString(),
                            FEHPrinter.printUnit(unit, false)));

                new Menu(e.getAuthor(), e.getChannel(), header, entries);
                return;
            }
        }

        sendMessage("could not find your barracks. Have you summoned yet?");
    }



    public String getName() { return "Allies"; }
    public String getDescription() { return "A place to view your barracks—all the allies you've summoned!"; }
    public String getFullDescription() {
        return "Currently a work in progress: " +
                "only allows a user to view a list of names of the units they have acquired.\n\n" +
                "In the future, this command will function similarly to the real Allies section," +
                "providing team-building and other ally-manipulation tools.";
    }
}
