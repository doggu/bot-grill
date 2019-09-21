package discordUI.feh;

import discordUI.menu.Menu;
import discordUI.menu.MenuEntry;
import feh.heroes.unit.Unit;
import feh.players.Barracks;
import feh.players.Summoner;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.ArrayList;

public class BarracksWindow extends Menu {
    public BarracksWindow(Summoner summoner, MessageChannel channel, Message header) {
        super(summoner.getUser(), channel, header, generateEntries(summoner.getBarracks()));
    }

    private static ArrayList<MenuEntry> generateEntries(Barracks b) {
        ArrayList<MenuEntry> heroes = new ArrayList<>();

        for (Unit x:b)
            heroes.add(new MenuEntry(x.getNickname(),
                    FEHPrinter.printUnit(x, false)));

        return heroes;
    }
}
