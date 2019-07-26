package utilities.math.unitConverter.units;

import java.util.HashMap;

import static utilities.math.unitConverter.units.SIBase.MASS;

public class Unit {
    private final String prefix;
    private final String symbol;
    private final SIBase baseUnit;
    private final int exponent;



    private static final HashMap<String,Double> SCALES;

    static {
        SCALES = new HashMap<>();
        HashMap<String, Integer> prefixes = new HashMap<>();
        prefixes.put("")
        SCALES.put("ys", 24.0);
        SCALES.put("zs", 21.0);
    }



    public Unit(String symbol, int exponent) throws UnknownUnitException {
        this.prefix = symbol.substring(0,symbol.indexOf(' '));
        this.symbol = symbol.substring(symbol.indexOf(' ')+1);
        this.exponent = exponent;


        switch(symbol) {
            case "g":
                baseUnit = MASS;
                break;
            default:
                throw new UnknownUnitException("unknown unit: "+symbol);
        }
    }



    public String getSymbol() { return symbol; }
    public SIBase getBaseUnit() { return baseUnit; }
    public int getExponent() { return exponent; }



    public static void main(String[] args) {
        String AG = "Angstrom-Galicc";
        Unit f;
        try {
            f = new Unit(AG);
        } catch (UnknownUnitException uue) {
            uue.printStackTrace();
            return;
        }

        switch (f.getBaseUnit()) {
            case TIME: System.out.println(f.getSymbol()+" is TIME!"); break;
            case DISTANCE: System.out.println(f.getSymbol()+" is DISTANCE!"); break;
            case MASS: System.out.println(f.getSymbol()+" is MASS!"); break;
            case CURRENT: System.out.println(f.getSymbol()+" is CURRENT!"); break;
            case TEMPERATURE: System.out.println(f.getSymbol()+" is TEMPERATURE!"); break;
            case QUANTITY: System.out.println(f.getSymbol()+" is QUANTITY!"); break;
            case LUMINOSITY: System.out.println(f.getSymbol()+" is LUMINOSITY!"); break;
        }
    }
}
