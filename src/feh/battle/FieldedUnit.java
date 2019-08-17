package feh.battle;

import feh.heroes.unit.Unit;

import java.awt.*;
import java.util.ArrayList;

public class FieldedUnit {
    //TODO: extending Unit might help in the future
    // but i dont remember why i didn't do it in the first place
    // so that's pretty sketchy
    // ok i remember why but maybe i can make it work later
    private final Unit unit;
    private int specialCD;
    private int currentHP;
    private int atkBonus, spdBonus, defBonus, resBonus;

    //heroes should know their own location to make allocating drives/ploys easier
    private Point pos;
    private boolean actionTaken;



    //effects (processed at turn starts, exiting combat, etc.) (future vision is exiting combat btw)
    private ArrayList<String> effects = new ArrayList<>();
    //examples: "Gravity" "Gjallarbrú" "Armor March"



    public FieldedUnit(Unit unit) {
        this.unit = unit;
        this.actionTaken = false;
    }



    public boolean canTakeAction() {
        return !actionTaken;
    }

    public Point getPos() { return pos; }



    public void applyEffects() {

    }

    public boolean move(Point newPos) {
        return true; //this requires board awareness, so maybe not
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