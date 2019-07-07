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



    private static final UnitConfiguration
            DISTANCE,
            VELOCITY,
            ACCELERATION,
            JERK,
            JOUNCE,

            MOMENTUM,
            ENERGY;


    static {
        DISTANCE = new UnitConfiguration(new int[] { 0,1,0,0,0,0,0 });
        VELOCITY = new UnitConfiguration(new int[] { -1,1,0,0,0,0,0 });
        ACCELERATION = new UnitConfiguration(new int[] { -2,1,0,0,0,0,0 });
        JERK = new UnitConfiguration(new int[] { -3,1,0,0,0,0,0 });
        JOUNCE = new UnitConfiguration(new int[] { -4,1,0,0,0,0,0 });

        MOMENTUM = new UnitConfiguration(new int[] { 0,1,1,0,0,0,0 });
        ENERGY = new UnitConfiguration(new int[] { -2,2,1,0,0,0,0 });
    }

    public String guessUnits(String unitsIn) {
        return "idk lol";
    }
}
