package events.commands;

import events.MessageListener;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public abstract class Command extends MessageListener {
    public abstract boolean isCommand();
    protected char getPrefix() { return '?'; }

    /*
    public static void process(GuildMessageReceivedEvent event) {
        String text = event.getMessage().getContentRaw();
        String[] args = text.substring(1).split(" ");

        String[] grCommands = {
                "getIVs",
                "getStats",
        };

        switch(args[0]) {
            case "chances":
                int r = (int)(Maffs.random()*100);
                event.getChannel().sendMessage(r+"%").queue();
                String log = "";
                for (String x:args) {
                    log+= " "+x;
                }
                log = "determined"+log+": "+r+"%";
                System.out.println(log);
                break;
            case "girl":



                break;
            default:
                for (String x:grCommands)
                    if (x.equals(args[0])) {
                        if (event.getChannel().getName().equals("gameroom"))
                            return;
                        else event.getChannel().sendMessage("sorry, that command is for the gameroom.\n" +
                                                                "head over there for access to this command.");
                    }
                event.getChannel().sendMessage("i don't recognize this command. sorry!").queue();
                break;
        }
    }
    */
}
