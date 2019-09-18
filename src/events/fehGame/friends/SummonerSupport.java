package events.fehGame.friends;

import events.fehGame.FEHCommand;

public class SummonerSupport extends FEHCommand {
    @Override
    public String getName() {
        return "SummonerSupport";
    }

    @Override
    public String getDescription() {
        return "who ya fuckin";
    }

    @Override
    public String getFullDescription() {
        return null;
    }

    @Override
    public boolean isCommand() {
        return false;
    }

    @Override
    public void onCommand() {

    }
}
