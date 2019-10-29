package stem.science.unitConverter.units;

import stem.science.unitConverter.UnitDatabase;
import utilities.Misc;

import java.util.HashMap;

public class ComplexUnit extends Unit {
    private final HashMap<Unit, Integer> units;



    public ComplexUnit(String name, String symbol, HashMap<Unit, Integer> units, double additionalScale) {
        super(name, symbol,
                absoluteConfiguration(units),
                absoluteScaleToSI(units, additionalScale));

        this.units = units;
    }

    public static HashMap<Unit, Integer> generateChildUnits(String unitsStr) {
        HashMap<Unit, Integer> units = new HashMap<>();
        String[] uStr = unitsStr.split("[-*/]");
        char[] joiners = new char[uStr.length];
        joiners[0] = '*';
        String copy = String.copyValueOf(unitsStr.toCharArray());
        copy = copy.replaceAll("-", "*");
        for (int i=1; i<joiners.length; i++) {
            int sI = copy.indexOf('/'), aI = copy.indexOf('*');
            if (sI<aI) {
                joiners[i] = '/';
            } else if (sI>aI) {
                joiners[i] = '*';
            } else {
                break;
            }
        }

        int i = 0;
        for (String s:uStr) {
            int exp = joiners[i]=='*'?1:-1;
            int p = s.indexOf('^');
            if (p>0) {
                exp*= Integer.parseInt(s.substring(p+1));
                s = s.substring(0, p);
            }

            Unit u = UnitDatabase.DATABASE.findBySymbol(s);

            units.put(u, exp);

            i++;
        }

        return units;
    }


    public static ComplexUnit generateUnit(String unit) {
        return generateUnit(new String(unit),new String(unit),new String(unit));
    }
    public static ComplexUnit generateUnit(String name, String symbol, String unit) {
        HashMap<Unit, Integer> units = generateChildUnits(unit);

        try {
            return new ComplexUnit(name, symbol, units, 1);
        } catch (Exception e) {
            return null;
        }
    }

    private static double absoluteScaleToSI(HashMap<Unit, Integer> a, double additionalScale) {
        double scale = 1;
        for (Unit u:a.keySet()) {
            int m = a.get(u);

            double s;
            if (m>0)
                s = u.scaleToSI();
            else if (m<0)
                s = 1/u.scaleToSI();
            else //wut
                continue;


            for (int i=0; i<Math.abs(m); i++) {
                scale*= s;
            }
        }

        return scale*additionalScale;
    }
    private static int[] absoluteConfiguration(HashMap<Unit, Integer> a) {
        int[] configuration = {0, 0, 0, 0, 0, 0, 0};

        for (Unit u:a.keySet()) {
            assert configuration != null;
            configuration = Misc.combine(configuration, u.getBaseUnits());
        }

        return configuration;
    }



    public boolean contains(Unit u) {
        for (Unit n:units.keySet()) {
            if (n instanceof ComplexUnit) {
                if (((ComplexUnit) n).contains(u)) {
                    return true;
                }
            } else {
                if (n==u) return true;
            }
        }

        return false;
    }

    public String toString() {
        return getName();
    }
}
