package stem.science.unitConverter;

import stem.science.unitConverter.units.InconversibleUnitsException;
import stem.science.unitConverter.units.Unit;
import utilities.data.Cereal;
import utilities.data.Database;
import utilities.data.WebCache;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Scanner;

public class UnitDatabase extends Database<Unit> implements Serializable {
    private static final String FILEPATH = "/units/";
    private static final int[][] BASE;

    public static UnitDatabase DATABASE;
    public static ArrayList<Unit> UNITS;

    private static final Cereal<UnitDatabase> DB_CEREAL;

    static {
        Cereal<UnitDatabase> DB_CEREAL1;
        boolean deserialized = false;
        try {
            DB_CEREAL1 = new Cereal<>("database.txt", FILEPATH);
            if ((DATABASE = DB_CEREAL1.deserialize())!=null) {
                deserialized = true;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            DB_CEREAL1 = null;
        }

        DB_CEREAL = DB_CEREAL1;
        BASE = new int[7][7];
        for (int i = 0; i < 7; i++) {
            BASE[i][i] = 1;
        }

        if (!deserialized) {
            System.out.println("constructing new UnitDatabase");
            DATABASE = new UnitDatabase();
            UNITS = DATABASE.getList();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (DB_CEREAL==null)
                    new Cereal<>("database.txt", FILEPATH).serialize(DATABASE);
                else
                    DB_CEREAL.serialize(DATABASE);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }));
    }

    @Override
    protected ArrayList<Unit> getList() {
        ArrayList<Unit> units = new ArrayList<>();

        //coincides with above 2d array (indexes of first dimensions correspond to each other)
        String[] nameSI = {
                "second",
                "meter",
                "gram",
                "ampere",
                "kelvin",
                "mole",
                "candela",
        };
        String[] symbolSI = {
                "s",
                "m",
                "g",
                "A",
                "K",
                "mol",
                "cd",
        };

        //index indicates order of magnitude difference (centered on 10)
        String[] prefixSI = {
                "yocto", null, null,
                "zepto", null, null,
                "atto", null, null,
                "femto", null, null,
                "pico", null, null,
                "nano", null, null,
                "micro", null, null,
                "milli",
                "centi",
                "deci",
                "",
                "deca",
                "hecto",
                "kilo", null, null,
                "mega", null, null,
                "giga", null, null,
                "tera", null, null,
                "peta", null, null,
                "exa", null, null,
                "zetta", null, null,
                "yotta",
        };
        String[] prefixSymbolSI = {
                "y", null, null,
                "z", null, null,
                "a", null, null,
                "f", null, null,
                "p", null, null,
                "n", null, null,
                "μ", null, null,
                "m",
                "c",
                "d",
                "",
                "da",
                "h",
                "k", null, null,
                "M", null, null,
                "G", null, null,
                "T", null, null,
                "P", null, null,
                "E", null, null,
                "Z", null, null,
                "Y",
        };

        for (int i=0; i<nameSI.length; i++) {
            for (int j=0; j<prefixSI.length; j++) {
                if (prefixSI[j]==null) //null magnitude spacer
                    continue;
                double scale = 1;
                double magnitude;
                if (j<24) {
                    magnitude = 0.1;
                } else {
                    magnitude = 10;
                }

                for (int n=Math.abs(24-j); n>0; n--) {
                    scale*= magnitude;
                }

                units.add(new Unit(
                        prefixSI[j]+nameSI[i],
                        prefixSymbolSI[j]+symbolSI[i],
                        BASE[i],
                        scale));
            }
        }

        //units.add(new Unit("second", "s", BASE[0], 1));
        //units.add(new Unit());
        //units.add(new Unit());
        //units.add(new Unit());
        //units.add(new Unit());
        //units.add(new Unit());
        //units.add(new Unit());
        //units.add(new Unit());
        //units.add(new Unit());
        //units.add(new Unit());
        //units.add(new Unit());

        return units;
    }

    @Override
    public Unit getRandom() {
        return null;
    }

    @Override
    public ArrayList<Unit> findAll(String input) {
        ArrayList<Unit> units = new ArrayList<>();
        for (Unit unit:UNITS) {
            if (unit.getName().equals(input)) units.add(unit);
        }

        return units;
    }

    public Unit findBySymbol(String symbol) {
        for (Unit unit:UNITS) {
            //todo: add regex field to Unit class for flexibility later
            if (unit.getSymbol().equals(symbol)) {
                return unit;
            }
        }

        return null;
    }

    @Override
    protected WebCache[] getOnlineResources() {
        return new WebCache[0];
    }



    public static void main(String[] a) {
        Scanner input = new Scanner(System.in);

        String line;
        while (!(line = input.nextLine()).equals("quit")) {
            String[] args = line.split(" ");

            if (args.length!=4)
                continue;

            double i;
            Unit s, e;

            try {
                i = Double.parseDouble(args[0]);
                s = DATABASE.findBySymbol(args[1]);
                e = DATABASE.findBySymbol(args[3]);
            } catch (Exception f) {
                continue;
            }

            if (!s.matches(e)) {
                System.out.println("mismatched units");
                continue;
            }

            try {
                System.out.println(
                        new BigDecimal(new Converter(s, e).apply(i))
                                .round(new MathContext(5)).toPlainString()
                                + " " + e.getSymbol());
            } catch (InconversibleUnitsException iue) {
                iue.printStackTrace();
            }
        }

        input.close();
    }
}
