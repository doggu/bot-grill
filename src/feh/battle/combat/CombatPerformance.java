package feh.battle.combat;

import feh.battle.FieldedUnit;

public class CombatPerformance {
    private final FieldedUnit unit;

    protected boolean didAttack;
    protected boolean triggeredSpecial;



    CombatPerformance(FieldedUnit unit) {
        this.unit = unit;
    }


    public boolean didAttack() {
        return didAttack;
    }

    public boolean triggeredSpecial() {
        return triggeredSpecial;
    }
}
