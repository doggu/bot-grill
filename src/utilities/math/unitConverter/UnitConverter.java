package utilities.math.unitConverter;

import net.dv8tion.jda.core.utils.tuple.Pair;

import javax.annotation.RegEx;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Function;

public class UnitConverter {
    private static final int
            TIME = 0,
            MASS = 1,
            LENGTH = 2,
            FORCE = 3,
            TEMPERATURE = 4,
            CURRENT = 5,
            QUANTITY = 6; //(mols)

    private static final String[][] SI_UNITS = {
            //indexes denotes the orders of magnitude each unit is away from each other
            { //time
                    "ys", "", "",
                    "zs", "", "",
                    "as", "", "",
                    "fs", "", "",
                    "ps", "", "",
                    "ns", "", "",
                    "µs", "", "",
                    "ms",
                    "cs",
                    "ds",
                    "s",
                    "das",
                    "hs",
                    "ks", "", "",
                    "Ms", "", "",
                    "Gs", "", "",
                    "Ts", "", "",
                    "Ps", "", "",
                    "Es", "", "",
                    "Zs", "", "",
                    "Ys",
            },
            { //length
                    "ym", "", "",
                    "zm", "", "",
                    "am", "", "",
                    "fm", "", "",
                    "pm", "", "",
                    "nm", "", "",
                    "µm", "", "",
                    "mm",
                    "cm",
                    "dm",
                    "m",
                    "dam",
                    "hm",
                    "km", "", "",
                    "Mm", "", "",
                    "Gm", "", "",
                    "Tm", "", "",
                    "Pm", "", "",
                    "Em", "", "",
                    "Zm", "", "",
                    "Ym",
            },
            { //mass
                    "yg", "", "",
                    "zg", "", "",
                    "ag", "", "",
                    "fg", "", "",
                    "pg", "", "",
                    "ng", "", "",
                    "µg", "", "",
                    "mg",
                    "cg",
                    "dg",
                    "g",
                    "dag",
                    "hg",
                    "kg", "", "",
                    "Mg", "", "",
                    "Gg", "", "",
                    "Tg", "", "",
                    "Pg", "", "",
                    "Eg", "", "",
                    "Zg", "", "",
                    "Yg",
            },
            { //electric current
                    "yA", "", "",
                    "zA", "", "",
                    "aA", "", "",
                    "fA", "", "",
                    "pA", "", "",
                    "nA", "", "",
                    "µA", "", "",
                    "mA",
                    "cA",
                    "dA",
                    "A",
                    "daA",
                    "hA",
                    "kA", "", "",
                    "MA", "", "",
                    "GA", "", "",
                    "TA", "", "",
                    "PA", "", "",
                    "EA", "", "",
                    "ZA", "", "",
                    "YA",
            },
            { //temperature
                    "K", // "C",
            },
            { //"amount of substance" (mol stuff thanks wikipedia)
                    "ymol", "", "",
                    "zmol", "", "",
                    "amol", "", "",
                    "fmol", "", "",
                    "pmol", "", "",
                    "nmol", "", "",
                    "µmol", "", "",
                    "mmol",
                    "cmol",
                    "dmol",
                    "mol",
                    "damol",
                    "hmol",
                    "kmol", "", "",
                    "Mmol", "", "",
                    "Gmol", "", "",
                    "Tmol", "", "",
                    "Pmol", "", "",
                    "Emol", "", "",
                    "Zmol", "", "",
                    "Ymol",
            },
            { //luminosity
                    "ycd", "", "",
                    "zcd", "", "",
                    "acd", "", "",
                    "fcd", "", "",
                    "pcd", "", "",
                    "ncd", "", "",
                    "µcd", "", "",
                    "mcd",
                    "ccd",
                    "dcd",
                    "cd",
                    "dacd",
                    "hcd",
                    "kcd", "", "",
                    "Mcd", "", "",
                    "Gcd", "", "",
                    "Tcd", "", "",
                    "Pcd", "", "",
                    "Ecd", "", "",
                    "Zcd", "", "",
                    "Ycd",
            }
    };



    private static final HashMap<String, Function<BigDecimal, BigDecimal>> CONVERSION_FUNCTIONS;

    static {
        CONVERSION_FUNCTIONS = new HashMap<>();
    }



    /**
     * converts a specific quantity into different units.
     * @param input the decimal number of the quantity
     * @param unitsIn the units of the quantity
     * @param unitsOut the desired units of the quantity
     * @return the new quantity in the desired units
     */
    public static BigDecimal convert(BigDecimal input, String unitsIn, String unitsOut) throws UnknownUnitException {
        UnitConfiguration
                configIn = new UnitConfiguration(),
                configOut = new UnitConfiguration();

        String[]
                argsIn = separate(unitsIn),
                argsOut = separate(unitsOut);

        int     u1 = -1,
                u2 = -1;

        for (String[] units:SI_UNITS) {
            for (int i = 0; i < units.length; i++) {
                if (unitsIn.equals(units[i])) {
                    //configIn.add(i, 1);
                    u1 = i;
                }
                if (unitsOut.equals(units[i])) {
                    //configOut.add(i, 1);
                    u2 = i;
                }
            }
            if (u1>=0&&u2>=0) break;
            u1 = -1;
            u2 = -1;
        }

        if (u1==-1) {
            throw new UnknownUnitException();
        }

        return input.multiply(new BigDecimal(Math.pow(10, u1-u2)));
    }



    private BigDecimal cToF(BigDecimal c) {
        return c.multiply(new BigDecimal(1.8))
                .add(new BigDecimal(32));
    }

    private BigDecimal fToC(BigDecimal f) {
        return f.subtract(new BigDecimal(32))
                .multiply(new BigDecimal(5))
                .divide(new BigDecimal(9), MathContext.UNLIMITED);
    }



    private static UnitConfiguration getUnitValues(String input) throws NumberFormatException {
        UnitConfiguration configuration = new UnitConfiguration();

        String[] args = separate(input);

        for (String x:args) {
            int config = 0;

            if (x.indexOf('^')>=0) {
                config = Integer.parseInt(x.substring(x.indexOf('^')+1));
                x = x.substring(0, x.indexOf('^'));
            }

            if (x.charAt(0)=='/')
                config*= -1;
            //otherwise it's either the first arg or '-' which is positive anyway

            for (int i=0; i<SI_UNITS.length; i++) {
                for (String unit:SI_UNITS[i]) {
                    if (x.equals(unit)) {
                        configuration.add(i, config);
                        break;
                    }
                }
            }
        }

        return configuration;
    }

    private static String[] separate(String input) {
        String[] args = input.split("[/-]");

        int fSlashI = input.indexOf('/');
        int nDashI = input.indexOf('-');

        int i = 1;

        while (fSlashI>=0) {
            if (nDashI>=0) {
                if (nDashI<fSlashI) {
                    args[i] = '-'+args[i];
                    input = input.substring(nDashI+1);
                    i++;
                }
            } else {
                args[i] = '/'+args[i];
                input = input.substring(fSlashI+1);
                i++;
            }

            fSlashI = input.indexOf('/');
            nDashI = input.indexOf('-');
        }

        while (nDashI>=0) {
            args[i] = '-'+args[i];
            input = input.substring(nDashI+1);
            i++;

            nDashI = input.indexOf('-');
        }

        return args;
    }



    public static void main(String[] uselessStuffSinceIDontKnowHowToUseJava) {
        Scanner input = new Scanner(System.in);

        while (input.hasNextLine()) {
            String line = input.nextLine();
            String[] args = line.split(" ");
            if (args.length!=2) continue;

            UnitConfiguration
                    in = getUnitValues(args[0]),
                    out = getUnitValues(args[0]);

            if (in.matches(out)) System.out.println("matches!");
            else System.out.println("inconversible...");
        }

        /*
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
        */

        input.close();
    }
}
