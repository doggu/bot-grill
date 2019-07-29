package utilities.science.unitConverter;

import utilities.science.unitConverter.units.Unit;
import utilities.science.unitConverter.units.UnknownUnitException;

import java.util.ArrayList;

public class UnitConfiguration {
    public static final int
            TIME = 0,
            LENGTH = 1,
            MASS = 2,
            CURRENT = 3,
            TEMPERATURE = 4,
            SUBSTANCE = 5,
            LUMINOSITY = 6;

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
        this.units = new ArrayList<>();

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



    private static ArrayList<Unit> getUnitValues(String input) throws NumberFormatException {
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
                configuration.add(new Unit(x));
            } catch (UnknownUnitException uue) {
                uue.printStackTrace();
                break;
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
