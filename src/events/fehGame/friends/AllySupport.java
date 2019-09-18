package events.fehGame.friends;

import events.fehGame.FEHCommand;

public class AllySupport extends FEHCommand {
    @Override
    public boolean isCommand() {
        return args[0].toLowerCase().matches("allysupport");
    }

    @Override
    public String getName() {
        return "AllySupport";
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
