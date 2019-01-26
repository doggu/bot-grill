package utilities.fehUnits.skills;

import java.util.ArrayList;
import java.util.Arrays;

public class PassiveA extends Passive implements StatModifier {
    private final int[] statModifiers;



    public PassiveA(String name, String description, int cost, boolean exclusive) {
        super (name, description, 'c', cost, exclusive);
        this.statModifiers = StatModifier.parseStatModifiers(description);
    }



    public int[] getStatModifiers() {
        return statModifiers;
    }
}
