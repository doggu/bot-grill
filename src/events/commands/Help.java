package events.commands;

public class Help extends Command {
    public boolean isCommand() {
        return args[0].equalsIgnoreCase("help");
    }

    public void onCommand() {

    }
}
