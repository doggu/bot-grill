package utilities.math.unitConverter;

import java.math.BigDecimal;
import java.util.Scanner;

public class UnitConverter {
    private static final int
            MASS = 0,
            DISPLACEMENT = 1;

    private static final String[][] UNITS = {
            //indexes denotes the orders of magnitude each unit is away from each other
            { //time
                    "ys", "","",
                    "zs", "","",
                    "as", "","",
                    "fs", "","",
                    "ps", "","",
                    "ns", "","",
                    "µs", "","",
                    "ms",
                    "cs",
                    "ds",
                    "s",
                    "das",
                    "hs",
                    "ks", "","",
                    "Ms", "","",
                    "Gs", "","",
                    "Ts", "","",
                    "Ps", "","",
                    "Es", "","",
                    "Zs", "","",
                    "Ys",
            },
            { //mass
                    "yg", "","",
                    "zg", "","",
                    "ag", "","",
                    "fg", "","",
                    "pg", "","",
                    "ng", "","",
                    "µg", "","",
                    "mg",
                    "cg",
                    "dg",
                    "g",
                    "dag",
                    "hg",
                    "kg", "","",
                    "Mg", "","",
                    "Gg", "","",
                    "Tg", "","",
                    "Pg", "","",
                    "Eg", "","",
                    "Zg", "","",
                    "Yg",
            },
            { //length
                    "ym", "","",
                    "zm", "","",
                    "am", "","",
                    "fm", "","",
                    "pm", "","",
                    "nm", "","",
                    "µm", "","",
                    "mm",
                    "cm",
                    "dm",
                    "m",
                    "dam",
                    "hm",
                    "km", "","",
                    "Mm", "","",
                    "Gm", "","",
                    "Tm", "","",
                    "Pm", "","",
                    "Em", "","",
                    "Zm", "","",
                    "Ym",
            },
            { //force
                    "N", //"lb",
            },
            { //temperature
                    "K", // "C",
            },
            { //electric current
                    "A",
            },
            { //"amount of substance" (mol stuff thanks wikipedia)
                    "mol",
            }
    };


    /**
     * converts a specific quantity into different units.
     * @param input the decimal number of the quantity
     * @param unitsIn the units of the quantity
     * @param unitsOut the desired units of the quantity
     * @return the new quantity in the desired units
     */
    public static BigDecimal convert(BigDecimal input, String unitsIn, String unitsOut) throws UnknownUnitException {
        int u1 = -1, u2 = -1;
        for (String[] units:UNITS) {
            for (int i = 0; i < units.length; i++) {
                if (unitsIn.matches(units[i])) u1 = i;
                if (unitsOut.matches(units[i])) u2 = i;
            }
            if (u1>=0&&u2>=0) break;
        }

        if (u1==-1||u2==-1) {
            throw new UnknownUnitException();
        }

        return input.multiply(new BigDecimal(Math.pow(10, u1-u2)));
    }



    public static void main(String[] uselessStuffSinceIDontKnowHowToUseJava) {
        Scanner input = new Scanner(System.in);

        while (input.hasNextLine()) {
            String[] args = input.nextLine().split(" ");
            if (args.length != 4) {
                System.out.println("improper length! please try again.");
                continue;
            }

            BigDecimal n = new BigDecimal(Long.parseLong(args[0]));
            String unitsIn = args[1];
            String unitsOut = args[3];

            BigDecimal result = null;
            try {
                result = convert(n, unitsIn, unitsOut);
            } catch (NumberFormatException nfe) {
                System.out.println("improper number format! please try again.");
            } catch (UnknownUnitException uue) {
                System.out.println("improper unit format! please try again.");
            }
            if (result==null) continue;

            System.out.println(n + " " + unitsIn + " is equivalent to " + result + " " + unitsOut);
        }

        input.close();
    }
}
