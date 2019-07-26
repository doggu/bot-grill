package utilities.math.unitConverter;

import utilities.math.unitConverter.units.Unit;
import utilities.math.unitConverter.units.UnknownUnitException;

import java.util.ArrayList;
import java.util.HashMap;

public class UnitConfiguration {
    public static final int
            TIME = 0,
            LENGTH = 1,
            MASS = 2,
            CURRENT = 3,
            TEMPERATURE = 4,
            SUBSTANCE = 5,
            LUMINOSITY = 6;


    public static final HashMap<String,UnitConfiguration> DERIVED_SI_UNITS;

    static {
        DERIVED_SI_UNITS = new HashMap<>();

        DERIVED_SI_UNITS.put("N", new UnitConfiguration("kg*m*s^-2"));
    }



    private ArrayList<Unit> units;

    private int[] unitPositions = {
            0,
            0,
            0,
            0,
            0,
            0,
            0,
    };


    
    public UnitConfiguration(String units) {
        this.units = getUnitValues(units);


        for (Unit x:this.units) {
            switch (x.getBaseUnit()) {
                case TIME:
                    unitPositions[0]+= x.getExponent();
                    break;
                case DISTANCE:
                    unitPositions[1]+= x.getExponent();
                    break;
                case MASS:
                    unitPositions[2]+= x.getExponent();
                    break;
                case CURRENT:
                    unitPositions[3]+= x.getExponent();
                    break;
                case TEMPERATURE:
                    unitPositions[4]+= x.getExponent();
                    break;
                case QUANTITY:
                    unitPositions[5]+= x.getExponent();
                    break;
                case LUMINOSITY:
                    unitPositions[6]+= x.getExponent();
                    break;
            }
        }
    }
    public UnitConfiguration(int[] unitPositions) {
        if (unitPositions.length!=7)
            throw new IndexOutOfBoundsException("expected length: 7 given index: "+unitPositions.length);

        this.unitPositions = unitPositions;
    }

    public void add(int field, int val) {
        unitPositions[field]+= val;
    }

    public int get(int field) {
        return unitPositions[field];
    }

    public boolean matches(UnitConfiguration units) {
        for (int i=0; i<unitPositions.length; i++) {
            if (units.unitPositions[i] != unitPositions[i]) {
                return false;
            }
        }
        return true;
    }





    private ArrayList<Unit> getUnitValues(String input) throws NumberFormatException {
        ArrayList<Unit> configuration = new ArrayList<>();

        String[] args = separate(input);

        for (String x:args) {
            int config = 1;
            if (x.indexOf('^')>=0) {
                config = Integer.parseInt(x.substring(x.indexOf('^')+1));
                x = x.substring(0, x.indexOf('^'));
            }

            if (x.charAt(0)=='/')
                config*= -1;
            //otherwise it's either the first arg or '-' which is positive anyway

            try {
                configuration.add(new Unit(x, config));

            } catch (UnknownUnitException uue) {
                uue.printStackTrace();
                break;
            }
        }

        return configuration;
    }

    private String[] separate(String input) {
        return input.split("\\*");

        /*
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
        */
    }



    private static final UnitConfiguration
            DISTANCE,
            VELOCITY,
            ACCELERATION,
            JERK,
            JOUNCE,

            MOMENTUM,
            ENERGY;


    static {
        DISTANCE = new UnitConfiguration(new int[] { 0,1,0,0,0,0,0 });
        VELOCITY = new UnitConfiguration(new int[] { -1,1,0,0,0,0,0 });
        ACCELERATION = new UnitConfiguration(new int[] { -2,1,0,0,0,0,0 });
        JERK = new UnitConfiguration(new int[] { -3,1,0,0,0,0,0 });
        JOUNCE = new UnitConfiguration(new int[] { -4,1,0,0,0,0,0 });

        MOMENTUM = new UnitConfiguration(new int[] { 0,1,1,0,0,0,0 });
        ENERGY = new UnitConfiguration(new int[] { -2,2,1,0,0,0,0 });
    }

    public String guessUnits(String unitsIn) {
        return "idk lol";
    }
}
