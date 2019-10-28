package stem.science.unitConverter.units;

import utilities.Misc;

import java.util.HashMap;

public class ComplexUnit extends Unit {




    public ComplexUnit(String name, String symbol, HashMap<Unit, Integer> units, double additionalScale) {
        super(name, symbol,
                absoluteConfiguration(units),
                absoluteScaleToSI(units, additionalScale));

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
}
