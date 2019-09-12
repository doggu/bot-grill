package utilities.science.chem;

//todo: rename to ChemicalElement? (clashing with org.jsoup.nodes.Element)
public class ChemicalElement {
    private final int atomicNumber;
    private final String symbol;
    private final String name;
    private final String origin;
    private final int group;
    private final int period;
    private final double atomicWeight;
    private final double density;
    private final double meltingPoint;
    private final double boilingPoint;
    private final double C;
    private final double electronegativity;
    private final double earthAbundance;

    ChemicalElement(
            int atomicNumber,
            String symbol,
            String name,
            String origin,
            int group,
            int period,
            double atomicWeight,
            double density,
            double meltingPoint,
            double boilingPoint,
            double C,
            double electronegativity,
            double earthAbundance) {
        this.atomicNumber = atomicNumber;
        this.symbol = symbol;
        this.name = name;
        this.origin = origin;
        this.group = group;
        this.period = period;
        this.atomicWeight = atomicWeight;
        this.density = density;
        this.meltingPoint = meltingPoint;
        this.boilingPoint = boilingPoint;
        this.C = C;
        this.electronegativity = electronegativity;
        this.earthAbundance = earthAbundance;
    }


    public int getAtomicNumber() { return atomicNumber; }

    public String getSymbol() { return symbol; }

    public String getName() { return name; }

    public String getOrigin() { return origin; }

    public int getGroup() { return group; }

    public int getPeriod() { return period; }

    public double getAtomicWeight() { return atomicWeight; }

    public double getDensity() { return density; }

    public double getMeltingPoint() { return meltingPoint; }

    public double getBoilingPoint() { return boilingPoint; }

    public double getC() { return C; }

    public double getElectronegativity() { return electronegativity; }

    public double getEarthAbundance() { return earthAbundance; }
}
