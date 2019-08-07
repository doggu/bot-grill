package events.devTools;

import events.MessageListener;
import feh.heroes.UnitDatabase;
import feh.heroes.character.Hero;

public class PortraitTest extends MessageListener {
    public boolean isCommand() { return args[0].equalsIgnoreCase("PortraitTest"); }
    public void onCommand() {
        if (args.length<2) {
            sendMessage("please include a hero name. (exact format only!)");
            return;
        }

        StringBuilder unitName = new StringBuilder(args[1]);
        for (int i=2; i<args.length; i++)
            unitName.append(' ').append(args[i]);

        String name = unitName.toString();

        for (Hero x:UnitDatabase.HEROES) {
            if (x.getFullName().toString().equals(name)) {
                sendMessage(x.getPortraitLink());
            }
        }
    }

    public char getPrefix() {
        return '&';
    }
}
