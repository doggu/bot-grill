package events.commands.Gamble;

import events.commands.Command;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Chances extends Command {
    public void onCommand() {
        int r = (int)(java.lang.Math.random()*100+1);
        sendMessage(r+"%");

        StringBuilder log = new StringBuilder("determined");
        for (String x:args)
            log.append(" ").append(x);
        log(log.append(": ").append(r).append("%").toString());
    }

    public boolean isCommand() {
        return args[0].equalsIgnoreCase("chances");
    }



    public String getName() { return "Chances"; }
    public String getDescription() { return "Test your luck!"; }
    public String getFullDescription() {
        return getDescription()+"\n" +
                "I will attempt to determine the likelihood of a certain event happening, good or bad.\n" +
                "\tSyntax: \"?Chances [event]\"\n" +
                "The text of [event] may be however long you like.";
    }
}
