package utilities.math.unitConverter.units;

import static utilities.math.unitConverter.units.SIBase.MASS;

public class Unit {
    private final String symbol;
    private final SIBase baseUnit;



    public Unit(String symbol) throws UnknownUnitException {
        this.symbol = symbol;


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
