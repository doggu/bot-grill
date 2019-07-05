package utilities.math.unitConverter;

public class UnitConfiguration {
    public static final int
            TIME = 0,
            LENGTH = 1,
            MASS = 2,
            CURRENT = 3,
            TEMPERATURE = 4,
            SUBSTANCE = 5,
            LUMINOSITY = 6;

    private int[] unitPositions = {
            0,
            0,
            0,
            0,
            0,
            0,
            0,
    };
    
    public UnitConfiguration() {}
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
}
