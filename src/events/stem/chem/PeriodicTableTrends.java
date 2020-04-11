package events.stem.chem;

import events.commands.Command;
import net.dv8tion.jda.core.MessageBuilder;

public class PeriodicTableTrends extends Command {
    private static final char
            SOUTHWEST = '↙', NORTHEAST = '↗', SOUTHEAST = '↘', THE_FUCK = '↫';

    @Override
    public void onCommand() {
        if (args.length==0) {
            sendMessage("wat");
        } else if (args.length==1) {
            //all of em
            sendMessage(new MessageBuilder()
                    .append(NORTHEAST).append(": \n")
                    .append("\tionization energy\n")
                    .append("\telectronegativity\n")
                    .append("\telectron affinity\n")
                    .append(SOUTHWEST).append(": \n")
                    .append("\tatomic radius\n")
                    .append(SOUTHEAST).append(": \n")
                    .append("\teffective nuclear charge\n").toString());
        } else if (args.length==2) {
            //one of em
            e.getMessage()
                    .addReaction(String.valueOf(getTrend(args[1])))
                    .complete();
        }
    }
    
    private static char getTrend(String name) {
        name = name.toLowerCase();
        if (name.matches("i(on(ization)?)?e(nergy)?") ||
            name.matches("e(lectro)?n(egativity)?") ||
            name.matches("e(lectron)?a(ff(inity)?)?")) {
            return NORTHEAST;
        } else if (name.matches("a(tom(ic)?)?r(adius)?")) {
            return SOUTHWEST;
        } else if (name.matches("e(ff(ective)?)?n(uclear)?c(harge)?")) {
            return SOUTHEAST;
        } else {
            return THE_FUCK;
        }
    }

    @Override
    public boolean isCommand() {
        //todo: remove typing status for this command
        // could probably solve a lot of my extending issues
        // by allowing Command to accept parameters
        // (like willType, or... other stuff)
        return args[0].toLowerCase()
                .matches("p(eriodic)?t(able)?t(rends)?");
    }

    @Override
    public String getName() {
        return "PeriodicTableTrends";
    }

    @Override
    public String getDescription() {
        return "Will answer trend questions " +
                "for various properties on various elements!";
    }

    @Override
    public String getFullDescription() {
        //TODO
        return null;
    }
}
