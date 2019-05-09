package events.commands.math;

import events.commands.Command;

import utilities.math.MathParse;

public class Maffs extends Command {



    public void onCommand() {
        if (args.length<2) {
            sendMessage("incorrect format!");
            return;
        }

        if (args.length==3) {
            double tv;
            try {
                tv = Double.parseDouble(args[1]);
            } catch (NumberFormatException g) {
                sendMessage("incorrect test value! please try again.");
                return;
            }
            sendMessage(args[2]+" evaulated at "+args[1]+": "+MathParse.parseProblem(args[2]).apply(tv));
        } else if (args.length==2) {
            if (args[1].contains("x"))
                sendMessage(args[1]+" evaulated at 1: "+MathParse.parseProblem(args[1]).apply(1.0));
            else
                sendMessage(MathParse.parseProblem(args[1]).apply(1.0));
        }
    }



    public boolean isCommand() { return args[0].equalsIgnoreCase("math"); }
}
