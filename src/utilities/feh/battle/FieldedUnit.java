package utilities.feh.battle;

import utilities.feh.heroes.unit.Unit;

public class FieldedUnit {
    private final Unit unit;
    private int currentHP;
    private int atkBonus, spdBonus, defBonus, resBonus;



    public FieldedUnit(Unit unit) {
        this.unit = unit;
    }
}
