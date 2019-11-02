package events.stem.chem;

import com.mojang.brigadier.Message;
import events.commands.Command;
import net.dv8tion.jda.core.MessageBuilder;

public class PeriodicTableTrends extends Command {
    private static final char SOUTHWEST = '↙', NORTHEAST = '↗', SOUTHEAST = '↘', THE_FUCK = '↫';

    @Override
    public void onCommand() {
        if (args.length==0) {
            sendMessage("wat");
        } else if (args.length==1) {
            //all of em
            MessageBuilder message = new MessageBuilder()
                    .append(NORTHEAST).append(": \n")
                    .append("\tionization energy\n")
                    .append("\telectronegativity\n")
                    .append("\telectron affinity\n")
                    .append(SOUTHWEST).append(": \n")
                    .append("\tatomic radius\n")
                    .append(SOUTHEAST).append(": \n")
                    .append("\teffective nuclear charge\n");
        } else if (args.length==2) {
            //one of em
            sendMessage(getTrend(args[1]));
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
        return args[0].toLowerCase().matches("p(eriodic)?t(able)?t(rends)?");
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getFullDescription() {
        return null;
    }
}
