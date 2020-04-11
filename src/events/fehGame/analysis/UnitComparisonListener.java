package events.fehGame.analysis;

import events.fehGame.FEHCommand;
import feh.characters.HeroDatabase;
import feh.characters.hero.Hero;

public class UnitComparisonListener extends FEHCommand {
    @Override
    public void onCommand() {
        //remove command keyword
        String[] args = new String[this.args.length-1];
        System.arraycopy(this.args, 1, args, 0, args.length);
        int pivot;
        for (pivot = 0; pivot<args.length; pivot++)
            if (args[pivot].equals("vs")) break;
        if (pivot==args.length) return;

        String[] firstUnit = new String[pivot-1],
                secondUnit = new String[args.length-(pivot+1)];
        System.arraycopy(args, 0, firstUnit,
                0, pivot-1);
        System.arraycopy(args, pivot+1, secondUnit,
                0, args.length-(pivot+1));

//        for (String x:firstUnit) System.out.print(x+"\t");
//        System.out.println();
//        for (String x:secondUnit) System.out.print(x+"\t");
//        System.out.println();

        //todo: write myself some ArrayUtils or smtg

        String first = "", second = "";
        for (String x:firstUnit)  //noinspection StringConcatenationInLoop
            first += " "+x;
        for (String x:secondUnit)  //noinspection StringConcatenationInLoop
            second += " "+x;
        first = first.substring(1);
        second = second.substring(1);

        Hero f, s;

        try {
            f = HeroDatabase.DATABASE.findAll(first).get(0);
            s = HeroDatabase.DATABASE.findAll(second).get(0);
        } catch (IndexOutOfBoundsException ioobe) {
            return;
        }

        int[][] f_stats_all = f.getAllStats(false, 5),
                s_stats_all = s.getAllStats(false, 5);

        int[] compare = new int[5];

        for (int i=0; i<5; i++) {
            compare[i] = f_stats_all[1][i] - s_stats_all[1][i];
        }

        String msg = "stats (first-second):";
        for (int i:compare)  //noinspection StringConcatenationInLoop
            msg += " "+i;

        sendMessage(msg);
    }

    @Override
    public boolean isCommand() {
        return args[0].equalsIgnoreCase("compare");
    }

    @Override
    public String getName() {
        return "Compare";
    }

    @Override
    public String getDescription() {
        return "Compares two units' stats.";
    }

    @Override
    public String getFullDescription() {
        return "i'll write this later i'm showing up my big dum dum friend";
    }
}
