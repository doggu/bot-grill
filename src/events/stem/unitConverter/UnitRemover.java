package events.stem.unitConverter;

import events.commands.Command;
import stem.science.unitConverter.UnitDatabase;
import stem.science.unitConverter.units.ComplexUnit;
import stem.science.unitConverter.units.Unit;

import java.util.ArrayList;

public class UnitRemover extends Command {
    @Override
    public void onCommand() {
        if (args.length!=2) {
            sendMessage("please only include one argument—" +
                    "the name or symbol of the unit.");
            return;
        }

        Unit unit = UnitDatabase.DATABASE.find(args[1]);

        if (unit==null) {
            unit = UnitDatabase.DATABASE.findBySymbol(args[1]);

            if (unit==null) {
                sendMessage("could not find your unit.");
                return;
            }
        }

        ArrayList<Unit> dependentUnits = new ArrayList<>();

        for (int i=0; i<UnitDatabase.UNITS.size(); i++) {
            Unit u = UnitDatabase.UNITS.get(i);
            if (u instanceof ComplexUnit) {
                if (((ComplexUnit) u).contains(unit)) {
                    dependentUnits.add(u);
                }
            }
        }
        /*
        if (dependentUnits.size()>0) {
            Message message = sendMessage("some units are dependent " +
                    "and will have to be removed. continue?\n" +
                    "```"+dependentUnits+"```");

            new PersonalButton(message,
                    e.getJDA().getEmotesByName("Accepted", false).get(0),
                    e.getAuthor()) {
                @Override
                public void onCommand() {
                    for (Unit x:dependentUnits)
                        UnitDatabase.UNITS.remove(x);

                    UnitRemover.this.sendMessage("removed: "+dependentUnits);
                }
            };
        } else {

         */
            UnitDatabase.UNITS.remove(unit);

            sendMessage("removed "+unit+".");
            log("removed "+unit+".");
        //}
    }

    @Override
    public boolean isCommand() {
        return args[0].toLowerCase().matches("remove");
    }

    @Override
    public String getName() {
        return "UnitRemover";
    }

    @Override
    public String getDescription() {
        return "remove units from the unit database";
    }

    @Override
    public String getFullDescription() {
        return "yes";
    }
}
