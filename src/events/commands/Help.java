package events.commands;

import main.BotMain;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Help extends Command {
    //read for active listeners so that help can take all of them and generate help commands based on their names
    
    public boolean isCommand() {
        return args[0].equalsIgnoreCase("help");
    }

    public void onCommand() {
        List<ListenerAdapter> listeners = BotMain.getListeners();
        ArrayList<Command> commands = new ArrayList<>();
        for (ListenerAdapter x : listeners)
            if (x instanceof Command)
                commands.add(((Command) x));

        if (args.length==1) {
            MessageBuilder message = new MessageBuilder(
                    "a list of things you can do!\n" +
                    "commands are called by placing a question mark before their name (e.x. \"?chances\")");
            for (int i=0; i<commands.size(); i++) {
                message.append("\n").append(i+1).append(". ").append(commands.get(i).getHelp());
                if (i+1>=10) {
                    //TODO: create multiple pages
                    break;
                }
            }

            sendMessage(message.build());
        } else if (args.length==2) {
            for (Command x:commands) {
                if (x.getName().equalsIgnoreCase(args[1])) {
                    sendMessage(x.getFullHelp());
                    break;
                }
            }
        }

        log("helped "+e.getAuthor().getName()+" understand me.");
    }

    public String getName() { return "Help"; }
    public String getDescription() { return "what you are seeing right now!"; }
    public String getFullDescription() {
        return "this is the command you are currently using! " +
                "i assume this is the developer testing my code.";
    }
}
