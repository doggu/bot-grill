package stem.science.unitConverter.units;

public enum SIBase {
    TIME(0),
    DISTANCE(1),
    MASS(2),
    CURRENT(3),
    TEMPERATURE(4),
    QUANTITY(5),
    LUMINOSITY(6);



    private final int i;



    SIBase(int index) {
        this.i = index;
    }

    public int i() { return i; }

    @Override
    public String toString() {
        switch (this) {
            case TIME:          return "TIME";
            case DISTANCE:      return "DISTANCE";
            case MASS:          return "MASS";
            case CURRENT:       return "CURRENT";
            case TEMPERATURE:   return "TEMPERATURE";
            case QUANTITY:      return "QUANTITY";
            case LUMINOSITY:    return "LUMINOSITY";
            default:            return "UNKNOWN";
        }
    }
}