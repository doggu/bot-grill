package events.fehGame;

import feh.heroes.UnitDatabase;
import feh.heroes.character.Hero;

import java.util.ArrayList;
import java.util.Arrays;

public class StatCheck extends FEHCommand {
    @Override
    public boolean isCommand() {
        return args[0].equalsIgnoreCase("Compare");
    }

    @Override
    public String getName() {
        return "Compare";
    }

    @Override
    public String getDescription() {
        return "Compare two different heroes!";
    }

    @Override
    public String getFullDescription() {
        return null;
    }

    @Override
    public void onCommand() {
        ArrayList<String> betterArgs = new ArrayList<>(Arrays.asList(
                e.getMessage().getContentRaw().substring(9)
                .split(" vs\\.? ")));

        if (betterArgs.size()<=1) {
            sendMessage("please include both heroes for comparison. " +
                    "make a distinction between them by placing a \"vs\" between them.");
            return;
        }

        Hero    h1 = UnitDatabase.DATABASE.find(betterArgs.get(0)),
                h2 = UnitDatabase.DATABASE.find(betterArgs.get(1));

        //h1.getStats()
    }
}
