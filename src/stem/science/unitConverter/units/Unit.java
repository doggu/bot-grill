package stem.science.unitConverter.units;

import static stem.science.unitConverter.units.SIBase.MASS;

public class Unit {
    private final String symbol;
    private final SIBase baseUnit;
    private final int exponent;



    public Unit(String unit, int exponent) throws UnknownUnitException {
        this.symbol = unit;
        this.exponent = exponent; //TODO: fuckign finish goid fucking daongmnint


        switch(unit) {
            case "g":
                baseUnit = MASS;
                break;
            default:
                throw new UnknownUnitException("unknown unit: "+unit);
        }
    }



    public String getSymbol() { return symbol; }
    public SIBase getBaseUnit() { return baseUnit; }
    public int getExponent() { return exponent; }



    public static void main(String[] args) {
        String AG = "Angstrom-Galicc";
        Unit f;
        try {
            f = new Unit(AG, 1);
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
