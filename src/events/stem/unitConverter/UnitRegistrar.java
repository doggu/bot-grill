package events.stem.unitConverter;

import events.commands.Command;
import stem.science.unitConverter.UnitDatabase;
import stem.science.unitConverter.units.ComplexUnit;
import stem.science.unitConverter.units.Unit;

import java.util.HashMap;

//todo: create task-specific listener ("?conv register ...")
public class UnitRegistrar extends Command {
    @Override
    public void onCommand() {
        if (args.length!=5) {
            sendMessage("i don't recognize this format");
            return;
        }

        String name = args[1],
               symbol = args[2];
        HashMap<Unit, Integer> units = ComplexUnit.generateChildUnits(args[3]);
        double scale = Double.parseDouble(args[4]);

        try {
            UnitDatabase.UNITS.add(new ComplexUnit(name, symbol, units, scale));
        } catch (Exception e) {
            sendMessage("unable to add your unit.");
            return;
        }

        sendMessage("added `"+name+"` (`"+symbol+"`).");
    }



    @Override
    public boolean isCommand() {
        return args[0].toLowerCase().matches("register");
    }

    @Override
    public String getName() { return "Register"; }
    @Override
    public String getDescription() {
        return "register new units to be used in conversions!";
    }
    @Override
    public String getFullDescription() {
        return "converts";
    }
}
