package events.commands;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Chances extends Command {
    public void onCommand() {
        int r = (int)(java.lang.Math.random()*100+1);
        sendMessage(r+"%");

        String log = "determined";
        for (String x:args)
            log+= " "+x;
        log(log+": "+r+"%");
    }

    public boolean isCommand() {
        return args[0].equalsIgnoreCase("chances");
    }
}
