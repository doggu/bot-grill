package events.commands.math;

import events.commands.Command;
import utilities.math.unitConverter.UnitConverter;
import utilities.math.unitConverter.UnknownUnitException;

import java.math.BigDecimal;

public class UnitConversionListener extends Command {
    public void onCommand() {
        if (args.length!=5) {
            sendMessage("incorrect format! please try again.");
            return;
        }
        
        BigDecimal n;
        try {
            n = new BigDecimal(Long.parseLong(args[1]));
        } catch (NumberFormatException nfe) {
            sendMessage("incorrect number format! please try again.");
            return;
        }
        
        String unitsIn = args[2];
        String unitsOut = args[4];
        
        BigDecimal result;
        try {
            result = UnitConverter.convert(n, unitsIn, unitsOut);
        } catch (UnknownUnitException uue) {
            sendMessage("sorry, i didn't understand one of your units. please try again.");
            return;
        }

        sendMessage(args[1]+" "+unitsIn+"\nis equivalent to "+result+" "+unitsOut);
        log("converted "+n+" "+unitsIn+" to "+result+" "+unitsOut+".");
    }


    public boolean isCommand() {
        return args[0].toLowerCase().matches("conv(ert)?");
    }

    public String getName() { return "UnitConversion"; }
    public String getDescription() { return "convert various units into other various units."; }
    public String getFullDescription() {
        return getDescription()+"\n" +
                "\tSyntax: \"?conv[ert] [qty1] [unit1] to [unit2]\"\n" +
                "qty1 in terms of unit1 will be converted in terms of unit2.\n" +
                "the only additional requirement is that " +
                "the two given units are measurements of the same concept.";

    }
}
