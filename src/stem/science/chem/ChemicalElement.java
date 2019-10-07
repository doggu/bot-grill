package stem.science.chem;

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
    protected ChemicalElement(int atomicNumber) {
        ChemicalElement toCopy = ElementDatabase.ELEMENTS.get(atomicNumber-1);
        this.atomicNumber = toCopy.atomicNumber;
        this.symbol = toCopy.symbol;
        this.name = toCopy.name;
        this.origin = toCopy.origin;
        this.group = toCopy.group;
        this.period = toCopy.period;
        this.atomicWeight = toCopy.atomicWeight;
        this.density = toCopy.density;
        this.meltingPoint = toCopy.meltingPoint;
        this.boilingPoint = toCopy.boilingPoint;
        this.C = toCopy.C;
        this.electronegativity = toCopy.electronegativity;
        this.earthAbundance = toCopy.earthAbundance;
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

    /*
    from left to right:

    other non-metal (hydrogen)
    alkali metal
    alkaline earth metal
    lanthanide
    actinide
    transition metal
    unknown (Mt, Ds, Rg, Uut, Fl, Uup, Lv, Uus)
    post-transition metal
    metalloid
    other non-metal (right)
    halogen
    noble gas

    oth N-mtl
    alk mtl
    alk E-mtl
    lanthanide
    actinide
    tr mtl
    unknown
    post-tr
    metalloid
    oth N-mtl (right)
    halogen
    noble gas
     */

    //universal truth about knowledge
    private static final String
            IDK = "unknown";

    //wikipedia classification
    private static final String
            NON_M = "nonmetal",
            METAL = "metal",
            MTLD = "metalloid";

    //other definitions
    /*
    private static final String
            ALK_M = "alkali metal",
            ALK_E_M = "alkaline earth metal",
            LANTH = "lanthanide",
            ACT = "actinide",
            TR_M = "transition metal";
     */

    public String chemicalProperties() {
        switch (period) {
            case 1:
                return NON_M;
            case 2:
                switch (Integer.compare(group, 13)) {
                    case -1:
                        return METAL;
                    case 0:
                        return MTLD;
                    case 1:
                        return NON_M;
                }
            case 3:
                switch (Integer.compare(group, 14)) {
                    case -1:
                        return METAL;
                    case 0:
                        return MTLD;
                    case 1:
                        return NON_M;
                }
            case 4:
                switch (Integer.compare(group, 14)) {
                    case -1:
                        return METAL;
                    case 0:
                        return MTLD;
                    case 1:
                        break; //return group==15?MTLD:NON_M; if this works i'm big brain
                }
            case 5:
                switch (Integer.compare(group, 15)) {
                    case -1:
                        return METAL;
                    case 0:
                        return MTLD;
                    case 1:
                        return group==16?MTLD:NON_M;
                }
            case 6:
                switch (Integer.compare(group, 17)) {
                    case -1:
                        return METAL;
                    case 0:
                        return MTLD;
                    case 1:
                        return NON_M;
                }
            case 7:
                if (group<=8)
                    return METAL;
                else
                    switch (group) {
                        case 12:
                        case 14:
                            return METAL;
                        /*
                        case 9:
                        case 10:
                        case 11:
                        case 13:
                        case 15:
                        case 16:
                        case 17:
                        case 18:
                        */
                        default:
                            return IDK;
                    }
            case 8:
                return IDK;
        }

        return "idk lol";
    }
}
