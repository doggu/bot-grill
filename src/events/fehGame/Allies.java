package events.fehGame;

import events.commands.Command;
import events.fehGame.retriever.HeroRetriever;
import utilities.feh.players.Barracks;
import utilities.feh.players.Summoner;

public class Allies extends Command {
    public boolean isCommand() {
        return args[0].equalsIgnoreCase("getAllies");
    }

    public void onCommand() {
        for (Summoner x:SummonSimulator.summoners) {
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
                        sendMessage(HeroRetriever.printUnit(barracks.get(allyIndex-1),false));
                        log("provided data on "+x.getName()+"\'s unit");
                        return;
                    } catch (NumberFormatException g) {
                        switch(args[1].toLowerCase()) {
                            case "atk":
                                barracks.sortByAtk();
                                break;
                            default:
                                //it's sorted by however it was before i guess
                        }
                    }
                }
                StringBuilder message = new StringBuilder("You have ")
                        .append(barracks.size())
                        .append(" units.\n\n");

                for (int i=0; i<barracks.size(); i++) {
                    message.append((i + 1))
                            .append(". ")
                            .append(barracks.get(i))
                            .append('\n');
                    if ((i+1)%50==0) {
                        sendMessage(message.toString());
                        message = new StringBuilder();
                    }
                }
                sendMessage(message);
                return;
            }
        }

        sendMessage("could not find your barracks. Have you summoned yet?");
    }
}
