package events.gameroom;

import events.commands.Gameroom;
import net.dv8tion.jda.core.entities.Message;

public class SummonSimulator extends Gameroom {

    public void onCommand() {
        //"Your circle for: "+[banner title]+"\n"
        //"rates: focus = " +[focus rate]+" 5* = "+[5* rate]
        Message circle = sendMessage("sum stones");

        e.getJDA().addEventListener(new CircleSimulator(circle, e.getAuthor()));
    }

    public boolean isCommand() {
        return args[0].equalsIgnoreCase("summon");
    }
}
