package events.commands.math;

import events.commands.Command;

public class UnitConversion extends Command {
    private static final String[][] UNITS = {
            { //mass
                    "yg",
                    "zg",
                    "ag",
                    "fg",
                    "pg",
                    "ng",
                    "µg",
                    "mg",
                    "cg",
                    "dg",
                    "g",
                    "dag",
                    "hg",
                    "kg",
                    "Mg",
                    "Gg",
                    "Tg",
                    "Pg",
                    "Eg",
                    "Zg",
                    "Yg",
            },
            { //length
                    "ym",
                    "zm",
                    "am",
                    "fm",
                    "pm",
                    "nm",
                    "µm",
                    "mm",
                    "cm",
                    "m",
                    "dm",
                    "dam",
                    "hm",
                    "km",
                    "Mm",
                    "Gm",
                    "Tm",
                    "Pm",
                    "Em",
                    "Zm",
                    "Ym",
            },
            { //surface area
                    "ym^2",
                    "zm^2",
                    "am^2",
                    "fm^2",
                    "pm^2",
                    "nm^2",
                    "µm^2",
                    "mm^2",
                    "cm^2",
                    "m^2",
                    "dm^2",
                    "dam^2",
                    "hm^2",
                    "km^2",
                    "Mm^2",
                    "Gm^2",
                    "Tm^2",
                    "Pm^2",
                    "Em^2",
                    "Zm^2",
                    "Ym^2",
            },
            { //volume
                    "ym^3",
                    "zm^3",
                    "am^3",
                    "fm^3",
                    "pm^3",
                    "nm^3",
                    "µm^3",
                    "mm^3",
                    "cm^3",
                    "m^3",
                    "dm^3",
                    "dam^3",
                    "hm^3",
                    "km^3",
                    "Mm^3",
                    "Gm^3",
                    "Tm^3",
                    "Pm^3",
                    "Em^3",
                    "Zm^3",
                    "Ym^3",
            },
            { //momentum
                    "Ns",
                    "kg-m/s",
                    "Angstrom-Galiccs?",
            }
    };



    public void onCommand() {
        if (args.length!=5) {
            sendMessage("incorrect format! please try again.");
            return;
        }

        double n;
        try {
            n = Double.parseDouble(args[1]);
        } catch (NumberFormatException f) {
            sendMessage("incorrect number format! please try again.");
            return;
        }

        int u1 = -1, u2 = -1;
        for (String[] units:UNITS) {
            for (int i = 0; i < units.length; i++) {
                if (args[2].matches(units[i])) u1 = i;
                if (args[4].matches(units[i])) u2 = i;
            }
            if (u1>=0&&u2>=0) break;
        }

        if (u1==-1||u2==-1) {
            sendMessage("sorry, i did not recognize one of your units.");
            return;
        }

        double result = n*Math.pow(10, u2-u1);

        sendMessage(args[1]+" "+args[2]+"\nis equivalent to "+result+" "+args[4]);
        log("converted "+args[1]+" "+args[2]+" to "+result+" "+args[4]+".");
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
