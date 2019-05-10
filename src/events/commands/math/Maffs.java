package events.commands.math;

import events.commands.Command;

import utilities.math.MathParse;

public class Maffs extends Command {



    public void onCommand() {
        String[] temp = args.clone();
        args = new String[args.length-1];
        for (int i=0; i<args.length; i++)
            args[i] = temp[i+1];
        if (args.length==0)
            return;

        if (args.length==2) {
            double tv;
            try {
                tv = Double.parseDouble(args[0]);
            } catch (NumberFormatException g) {
                sendMessage("incorrect test value! please try again.");
                return;
            }
            sendMessage(args[1] + " evaulated at " + args[0] + ": " + new MathParse(args[1]).getFunction().apply(tv));
        } else if (args.length == 1) {
            if (args[0].contains("x"))
                sendMessage(args[0] + " evaulated at 1: " + new MathParse(args[0]).getFunction().apply(1.0));
            else
                sendMessage(new MathParse(args[0]).getFunction().apply(1.0));
        }
    }



    public boolean isCommand() {
        if (args[0].equalsIgnoreCase("math")) return true;
        return e.getChannel().getName().equals("math");
    }
}
