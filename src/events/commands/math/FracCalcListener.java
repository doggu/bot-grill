package events.commands.math;

import events.commands.Command;
import utilities.math.FracCalc;

public class FracCalcListener extends Command {
    public boolean isCommand() {
        return args[0].toLowerCase().equals("fraccalc");
    }

    public void onCommand() {
        String input = "";
        for (int i=1; i<args.length; i++)
            input+= " "+args[i];
        input = input.substring(1);

        try {
            String answer = FracCalc.produceAnswer(input);
            sendMessage(answer);
            log("FracCalc'd "+input+": "+answer);
        } catch (Error f) {
            sendMessage("incorrect format! please try again.");
            log(e.getAuthor().getName()+" attempted to FracCalc \""+input+"\"");
        }
    }



    public String getName() { return "FracCalc"; }
    public String getDescription() { return "add, subtract, multiply, or divide fractions!"; }
    public String getFullDescription() {
        //TODO: write DESCRIPTon
        return "fuck man it's complicated";
    }
}
