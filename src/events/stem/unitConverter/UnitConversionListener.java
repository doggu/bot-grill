package events.stem.unitConverter;

import events.commands.Command;
import stem.math.PrettyNumber;
import stem.science.unitConverter.Converter;
import stem.science.unitConverter.units.ComplexUnit;
import stem.science.unitConverter.units.InconversibleUnitsException;
import stem.science.unitConverter.units.Unit;

import java.math.BigDecimal;

public class UnitConversionListener extends Command {
    public void onCommand() {
        boolean ginormo = true;

        if (args.length<5) {
            sendMessage("incorrect format! please try again.");
            return;
        } else if (args.length>5) {
            for (int i=5; i<args.length; i++) {
                switch(args[i]) {
                    case "-sci":
                        ginormo = false;
                        break;
                    case "-ginormo":
                        ginormo = true;
                        break;
                }
            }
        }
        
        BigDecimal n;
        try {
            n = new BigDecimal(Double.parseDouble(args[1]));
        } catch (NumberFormatException nfe) {
            sendMessage("incorrect number format! please try again.");
            return;
        }
        Unit unitsIn, unitsOut;
        try {
            unitsIn =  ComplexUnit.generateUnit(args[2]);
            unitsOut = ComplexUnit.generateUnit(args[4]);
        } catch (Exception e) {
            sendMessage("one of your units didn't compile properly. please try again.");
            return;
        }

        BigDecimal result;
        try {
            result = new BigDecimal(
                    new Converter(unitsIn, unitsOut).apply(n.doubleValue()));
        } catch (InconversibleUnitsException iue) {
            sendMessage("your units are not compatible. please try again.");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            sendMessage("there was an error during conversion. please try again.");
            return;
        }

        sendMessage("`"+args[1]+" "+unitsIn+"`\nis equivalent to:\n`" +
                new PrettyNumber(result, ginormo)+" "+unitsOut+"`");

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
