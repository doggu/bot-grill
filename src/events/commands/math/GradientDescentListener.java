package events.commands.math;

import events.commands.Command;
import stem.math.GradientDescent;
import stem.math.MathParse;

import java.util.function.Function;

public class GradientDescentListener extends Command {
    private final double ACCURACY = 0.000001;

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

        Function<Double,Double> fxnPrime = x -> (fxn.apply(x)-fxn.apply(x-ACCURACY))/ACCURACY;

        double minimum = GradientDescent.gradientDescent(fxnPrime, start);

        sendMessage("the minimum for f(x)="+args[2]+" near "+start+" is: "+minimum);
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
