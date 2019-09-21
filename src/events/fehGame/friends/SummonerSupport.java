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
        return args[0].toLowerCase().matches("s(ummoner)?s(upport)?");
    }

    @Override
    public void onCommand() {
        if (args.length<2) {
            sendMessage("please specify who you would like to support:");
            //barracks window here with selection event listener
        }
    }
}
