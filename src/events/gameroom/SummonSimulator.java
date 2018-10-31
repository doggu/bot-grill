package events.gameroom;

import net.dv8tion.jda.core.entities.Message;
import utilities.fehUnits.summoning.Banner;
import utilities.fehUnits.summoning.BannerDatabase;

import java.util.List;

public class SummonSimulator extends Gameroom {

    public void onCommand() {
        //"Your circle for: "+[banner title]+"\n"
        //"rates: focus = " +[focus rate]+" 5* = "+[5* rate]

        List<Banner> list = BannerDatabase.getList();
        Banner randomBanner = list.get((int)(Math.random()*list.size())); //temp
        e.getJDA().addEventListener(
                new CircleSimulator(sendMessage("your summons for: "+randomBanner.getName()),
                        e.getAuthor(),
                        randomBanner));
    }

    public boolean isCommand() {
        return args[0].equalsIgnoreCase("summon");
    }
}
