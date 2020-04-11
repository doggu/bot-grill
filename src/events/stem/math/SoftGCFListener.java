package events.stem.math;

import events.commands.Command;
import utilities.Misc;

import java.util.ArrayList;
import java.util.Arrays;

public class SoftGCFListener extends Command {
    @Override
    public void onCommand() {
        ArrayList<String> args = new ArrayList<>(Arrays.asList(this.args));

        args.remove(0);

        double[] nums = new double[args.size()];

        for (int i = 0; i<args.size(); i++)
            nums[i] = Double.parseDouble(args.get(i));

        sendMessage(Misc.softGCF(nums));
    }

    @Override
    public boolean isCommand() {
        return args[0].toLowerCase().matches("s(oft)?gcf");
    }

    @Override
    public String getName() {
        return "SoftGCF";
    }

    @Override
    public String getDescription() {
        return "calculates an estimated GCF with double precision.";
    }

    @Override
    public String getFullDescription() {
        return "it usually ends up going to the lowest decimal place if the " +
                "numbers are small enough so idk how useful this really is";
    }
}
