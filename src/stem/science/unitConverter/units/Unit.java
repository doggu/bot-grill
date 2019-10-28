package stem.science.unitConverter.units;



public class Unit {
    private final String name, symbol;
    private final int[] baseUnits;
    private final double scaleToSI;



    public Unit(String name, String symbol, int[] configuration, double scaleToSI) {
        this.name = name;
        this.symbol = symbol;
        this.baseUnits = configuration;
        this.scaleToSI = scaleToSI;
    }


    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public int[] getBaseUnits() { return baseUnits; }

    public double scaleToSI() {
        return scaleToSI;
    }

    /**
     * determines if two units represent the same quantity (e.g. meters and yards)
     * @param f the unit to test against
     * @return true if both units indicate the same quantity, false otherwise
     *         (and if there's some indexing issue i guess)
     */
    public boolean matches(Unit f) {
        try {
            for (int i = 0; i < baseUnits.length; i++) {
                if (this.baseUnits[i]!=f.baseUnits[i]) return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String toString() { return symbol; }
}
