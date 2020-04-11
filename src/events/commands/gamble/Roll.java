package events.commands.gamble;

import events.commands.Command;

public class Roll extends Command {
    public boolean isCommand() {
        return args[0].equalsIgnoreCase("Roll"); }

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

        if (range<0) minimum++;

        sendMessage("<@"+e.getAuthor().getId()+"> rolled a " +
                (int)(Math.random()*range+minimum)+".");
    }


    public String getName() { return "Roll"; }
    public String getDescription() { return "Make a quick decision!"; }
    public String getFullDescription() {
        return "\tSyntax: \"?Roll\" OR \"?Roll [range]\"\n" +
                "[range] will determine what numbers, " +
                "from 1 to n, you can roll. " +
                "Yes, it works with negative numbers as well.";
    }
}
