package events.stem.math;

import events.commands.Command;
import stem.math.FracCalc;

public class FracCalcListener extends Command {
    public boolean isCommand() {
        return args[0].toLowerCase().equals("fraccalc");
    }

    public void onCommand() {
        StringBuilder inputSB = new StringBuilder();
        for (int i=1; i<args.length; i++)
            inputSB.append(" ").append(args[i]);
        String input = inputSB.substring(1);

        try {
            String answer = FracCalc.produceAnswer(input);
            sendMessage(answer);
            log("FracCalc'd "+input+": "+answer);
        } catch (Error f) {
            sendMessage("incorrect format! please try again.");
            log(e.getAuthor().getName()+" attempted to FracCalc \""+input+"\"");
        }
    }


    public String getName() {
        return "FracCalc";
    }

    public String getDescription() {
        return "add, subtract, multiply, or divide fractions!";
    }

    public String getFullDescription() {
        return "it's complicated man";
    }
}
