package events.commands.help;

import events.commands.Command;
import discordUI.menu.*;
import main.BotMain;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Help extends Command {
    //read for active listeners so that help can take all of
    //them and generate help commands based on their names
    
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
            Message header = new MessageBuilder(
                    "a list of things you can do!\n" +
                    "commands are called by placing a question mark " +
                            "before their name (e.x. \"?chances\")")
                    .build();



            ArrayList<MenuEntry> entries = new ArrayList<>(commands.size());

            for (Command c:commands) {
                MenuEntry entry = new MenuEntry(
                        c.getName()+": "+c.getDescription());

                entry.setAuthor(c.getName())
                     .setDescription(c.getDescription());

                entries.add(entry);
            }


            new Menu(e.getAuthor(), e.getChannel(), header, entries);
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
