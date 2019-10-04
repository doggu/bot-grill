package events.commands.stem.chem;

import events.commands.Command;

public class Soluble extends Command {

    @Override
    public void onCommand() {
        if (args.length!=6) {
            sendMessage("please provide two compounds you wish to compare.");
        }
    }

    @Override
    public boolean isCommand() {
        return args[0].toLowerCase().matches("s(oluble)?");
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
