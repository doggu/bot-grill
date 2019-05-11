package events.commands.math;

import events.commands.Command;

import net.dv8tion.jda.core.entities.User;
import utilities.math.MathParse;

import java.util.HashMap;

public class Maffs extends Command {
    private HashMap<User,Double> answers = new HashMap<>();


    public void onCommand() {
        String[] temp = args.clone();
        args = new String[args.length-1];
        for (int i=0; i<args.length; i++)
            args[i] = temp[i+1];
        if (args.length==0)
            return;

        double value;
        if (args.length==2) {
            double tv;
            try {
                tv = Double.parseDouble(args[0]);
            } catch (NumberFormatException g) {
                sendMessage("incorrect test value! please try again.");
                return;
            }
            if (args[1].contains("ans")) {
                double ans;
                try {
                    ans = answers.get(e.getAuthor());
                } catch (NullPointerException g) {
                    sendMessage("no previous answer was found!");
                    return;
                }
                args[1] = args[1].replace("ans",""+ans);
            }
            value = new MathParse(args[1]).getFunction().apply(tv);
            sendMessage(args[1] + " evaulated at " + args[0] + ": " + value);
        } else if (args.length == 1) {
            if (args[0].contains("ans")) {
                double ans;
                try {
                    ans = answers.get(e.getAuthor());
                } catch (NullPointerException g) {
                    sendMessage("no previous answer was found!");
                    return;
                }
                args[0] = args[0].replace("ans",""+ans);
            }
            value = new MathParse(args[0]).getFunction().apply(1.0);
            if (args[0].contains("x"))
                sendMessage(args[0] + " evaulated at 1: " + value);
            else
                sendMessage(value);
        } else return;

        answers.put(e.getAuthor(), value);
    }

    public boolean isCommand() {
        if (args[0].equalsIgnoreCase("math")) return true;
        return e.getChannel().getName().equals("math");
    }



    public String getName() { return "Math"; }
    public String getDescription() { return "perform basic math!"; }
    public String getFullDescription() {
        //TODO: write DESCRIPTon
        return getDescription()+"\n" +
                "fuck man it's complicated";
    }
}
