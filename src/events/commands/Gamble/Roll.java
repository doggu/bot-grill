package events.commands.Gamble;

import events.commands.Command;

public class Roll extends Command {
    public boolean isCommand() { return args[0].equalsIgnoreCase("Roll"); }
    public void onCommand() {
        int minimum = 1;
        int range = 10;
        if (args.length>1) {
            try {
                range = Integer.parseInt(args[1]);
            } catch (NumberFormatException f) {
                //it's fine
            }
        }

        sendMessage("<@"+e.getAuthor().getId()+"> rolled a "+(int)(Math.random()*range+minimum)+".");
    }
}
