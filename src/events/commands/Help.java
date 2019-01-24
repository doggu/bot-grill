package events.commands;

public class Help extends Command {
    //read for active listeners so that help can take all of them and generate help commands based on their names
    
    public boolean isCommand() {
        return args[0].equalsIgnoreCase("help");
    }

    public void onCommand() {
        
    }
}
