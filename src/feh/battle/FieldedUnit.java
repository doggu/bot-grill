package feh.battle;

import feh.heroes.unit.Unit;

import java.awt.*;

public class FieldedUnit {
    //TODO: extending Unit might help in the future
    // but i dont remember why i didn't do it in the first place
    // so that's pretty sketchy
    private final Unit unit;
    private int currentHP;
    private int atkBonus, spdBonus, defBonus, resBonus;

    //heroes should know their own location to make allocating drives/ploys easier
    private Point location;
    private boolean actionTaken;



    public FieldedUnit(Unit unit) {
        this.unit = unit;
    }



    public boolean move(Point newPos) {
        return true; //this requires board awareness, so maybe not
    }

    public boolean canTakeAction() {
        return !actionTaken;
    }




}