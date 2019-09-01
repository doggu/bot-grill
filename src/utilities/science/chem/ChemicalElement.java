package utilities.science.chem;

//todo: rename to ChemicalElement? (clashing with org.jsoup.nodes.Element)
public class ChemicalElement {
    private final String
            atomicNumber,
            symbol,
            name,
            origin,
            group,
            period,
            atomicWeight,
            density,
            meltingPoint,
            boilingPoint,
            C,
            electronegativity,
            earthAbundance;

    ChemicalElement(
            String atomicNumber,
            String symbol,
            String name,
            String origin,
            String group,
            String period,
            String atomicWeight,
            String density,
            String meltingPoint,
            String boilingPoint,
            String C,
            String electronegativity,
            String earthAbundance) {
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


    public String getAtomicNumber() {return atomicNumber;}

    public String getSymbol() {return symbol;}

    public String getName() {return name;}

    public String getOrigin() {return origin;}

    public String getGroup() {return group;}

    public String getPeriod() {return period;}

    public String getAtomicWeight() {return atomicWeight;}

    public String getDensity() {return density;}

    public String getMeltingPoint() {return meltingPoint;}

    public String getBoilingPoint() {return boilingPoint;}

    public String getC() {return C;}

    public String getElectronegativity() {return electronegativity;}

    public String getEarthAbundance() {return earthAbundance;}
}
