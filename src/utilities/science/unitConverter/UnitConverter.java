package utilities.science.unitConverter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Scanner;

public class UnitConverter {
    /**
     * converts a specific quantity into different units.
     * @param input the decimal number of the quantity
     * @param unitsIn the units of the quantity
     * @param unitsOut the desired units of the quantity
     * @return the new quantity in the desired units
     */
    public static BigDecimal convert(BigDecimal input, String unitsIn, String unitsOut) {

        UnitConfiguration
                configIn = new UnitConfiguration(unitsIn),
                configOut = new UnitConfiguration(unitsOut);


        return new BigDecimal(5);
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
                    in = new UnitConfiguration(args[0]),
                    out = new UnitConfiguration(args[0]);

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
