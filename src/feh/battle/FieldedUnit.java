package feh.battle;

import feh.heroes.unit.Unit;

import java.awt.*;

public class FieldedUnit {
    //TODO: extending Unit might help in the future
    // but i dont remember why i didn't do it in the first place
    // so that's pretty sketchy
    private final Unit unit;
    private int specialCD;
    private int currentHP;
    private int atkBonus, spdBonus, defBonus, resBonus;

    //heroes should know their own location to make allocating drives/ploys easier
    private Point location;
    private boolean actionTaken;


    //effects (processed at turn starts, exiting combat, etc.) (future vision is exiting combat btw)



    public FieldedUnit(Unit unit) {
        this.unit = unit;
        this.actionTaken = false;
    }



    public boolean move(Point newPos) {
        return true; //this requires board awareness, so maybe not
    }

    public boolean canTakeAction() {
        return !actionTaken;
    }



    public FieldedUnit clone() {
        FieldedUnit clone = new FieldedUnit(unit);
        clone.specialCD = specialCD;
        clone.currentHP = currentHP;
        clone.atkBonus = atkBonus;
        clone.spdBonus = spdBonus;
        clone.defBonus = defBonus;
        clone.resBonus = resBonus;

        return clone;
    }
}