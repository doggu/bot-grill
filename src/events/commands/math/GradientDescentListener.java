package events.commands.math;

import events.commands.Command;
import utilities.math.GradientDescent;
import utilities.math.MathParse;

import java.util.function.Function;

public class GradientDescentListener extends Command {

    public void onCommand() {
        if (args.length<2) {
            sendMessage("please input a function to evaluate.");
            return;
        }
        double start;
        try {
            start = Double.parseDouble(args[1]);
        } catch (NumberFormatException g) {
            sendMessage("incorrect number format!");
            return;
        }
        MathParse function = new MathParse(args[2]);
        Function<Double,Double> fxn;
        try {
            fxn = function.getFunction();
        } catch (Error f) {
            sendMessage("incorrect function syntax! please try again.");
            return;
        }

        double minimum = GradientDescent.gradientDescent(fxn, start);

        sendMessage("the minimum for "+start+" is: "+minimum);
    }

    public boolean isCommand() {
        return args[0].equalsIgnoreCase("GD");
    }



    public String getName() { return "GradientDescent"; }
    public String getDescription() { return "Find the local minimum of a function."; }
    public String getFullDescription() {
        return getDescription()+"\n"+
                "Syntax: \"?GD [function]\"\n" +
                "similar to the math class, but processes the given function to find " +
                "the local minimum.";
    }
}
