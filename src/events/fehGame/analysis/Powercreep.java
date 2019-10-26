package events.fehGame.analysis;

import events.fehGame.FEHCommand;

public class Powercreep extends FEHCommand {
    @Override
    public boolean isCommand() {
        return args[0].toLowerCase().matches("p(ower)?c(reep)?");
    }

    @Override
    public String getName() {
        return "Powercreep";
    }

    @Override
    public String getDescription() {
        return "Determine if your unit has been outclassed!";
    }

    @Override
    public String getFullDescription() {
        return null;
    }

    @Override
    public void onCommand() {
        //todo: revisit
        /*
        if (args.length==1) {
            sendMessage("please provide a unit in the standard search method. " +
                    "if ambiguity is detected, the first hero, in alphabetical order, will be chosen.");
        }

        Hero victim = UnitDatabase.DATABASE.find(
                e.getMessage().getContentRaw().substring(e.getMessage().getContentRaw().indexOf(' ')+1));
        int[] victimStats = victim.getStats(false, 5, -1, -1);

        ArrayList<Hero> creepers = new ArrayList<>();



        for (Hero x:UnitDatabase.HEROES) {
            int[] powerStats = x.getStats(false, 5, -1, -1);

            int superiorStats = 0;
            for (int i=0; i<5; i++) {
                if (powerStats[i]>victimStats[i]) {
                    superiorStats++;
                }
            }

            if (superiorStats>=5) {
                creepers.add(x);
            }
        }

        StringBuilder message = new StringBuilder();

        for (Hero x:creepers) {
            int[] powerStats = x.getStats(false, 5, -1, -1);


            int[] comparison = new int[5];

            for (int i=0; i<5; i++) {
                comparison[i] = powerStats[i]-victimStats[i];
            }

            if (message.length()>1900) {
                sendMessage(message);
                message = new StringBuilder();
            }

            message.append(x.getFullName().toString()).append(" is superior!\n")
                    .append(FEHPrinter.printStats(comparison))
                    .append('\n');
        }

        if (message.length()>0)
            sendMessage(message);
        else
            sendMessage("you're safe from powercreep!");
         */
    }
}
