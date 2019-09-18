package events.commands.math;

import events.commands.Command;

import net.dv8tion.jda.core.entities.User;
import stem.math.MathParse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public class Maffs extends Command {
    private HashMap<User,Double> answers = new HashMap<>();
    private HashMap<User, ArrayList<Function<Double, Double>>> storedFunctions = new HashMap<>();


    public void onCommand() {
        if (args.length==1)
            return;

        String problem;
        double tv;
        if (args.length==3) {
            problem = args[2];
            try {
                tv = Double.parseDouble(args[1]);
            } catch (NumberFormatException g) {
                sendMessage("incorrect test value! please try again.");
                return;
            }
        } else if (args.length==2) {
            problem = args[1];
            tv = 1.0;
        } else return;

        if (problem.contains("ans")) {
            double ans;
            try {
                ans = answers.get(e.getAuthor());
            } catch (NullPointerException g) {
                sendMessage("no previous answer was found!");
                return;
            }
            String ansStr = String.valueOf(ans);
            ansStr = ansStr.replace("E", "*10^");
            problem = problem.replace("ans", "("+ansStr+")");
        }

        double value = new MathParse(problem).getFunction().apply(tv);

        if (problem.contains("x"))
            sendMessage(problem + " evaulated at "+tv+": " + value);
        else
            sendMessage(value);

        answers.put(e.getAuthor(), value);
    }

    public boolean isCommand() {
        if (args[0].equalsIgnoreCase("Math")) return true;
        if (e.getChannel().getName().equalsIgnoreCase("Math")) return true;

        return false;
    }



    public String getName() { return "Math"; }
    public String getDescription() { return "perform basic math!"; }
    public String getFullDescription() {
        //TODO: write DESCRIPTon
        return getDescription()+"\n" +
                "it's complicated man";
    }
}
