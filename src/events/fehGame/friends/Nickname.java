package events.fehGame.friends;

import events.fehGame.FEHCommand;

public class Nickname  extends FEHCommand {
    @Override
    public boolean isCommand() {
        return args[0].toLowerCase().matches("n(ick)?n(ame)?");
    }

    @Override
    public String getName() {
        return "Nickname";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getFullDescription() {
        return null;
    }

    @Override
    public void onCommand() {

    }
}
