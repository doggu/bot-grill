package feh.battle;

import feh.heroes.unit.Unit;

import java.awt.*;
import java.util.ArrayList;

public class FieldedUnit extends Unit {
    //TODO: extending Unit might help in the future
    // but i dont remember why i didn't do it in the first place
    // so that's pretty sketchy
    // ok i remember why but maybe i can make it work later
    // now i don't remember why again
    // why don't i make notes of this
    // ok i figured it out again
    // not sure if there's a way to create a super object based on an existing instance
    private final Unit unit;
    private int currentHP;
    private int specialCD;
    private int atkBonus, spdBonus, defBonus, resBonus;

    //heroes should know their own location to make allocating drives/ploys easier
    private Point pos;
    private boolean actionTaken;



    //effects (processed at turn starts, exiting combat, etc.) (future vision is exiting combat btw)
    // i wrote this but now i feel like i have to validate it again
    private ArrayList<String> effects = new ArrayList<>();
    //examples: "Gravity" "Gjallarbrú" "Armor March"



    public FieldedUnit(Unit unit) {
        //todo: keep track of fields since the original unit object is not the same instance as the super
        // could lead to missing sp/hm or other issues
        super(unit);
        this.unit = unit;
        this.actionTaken = false;
    }



    public boolean canTakeAction() {
        return !actionTaken;
    }

    public Point getPos() { return pos; }



    //what does this do again
    public void applyEffects() {

    }

    public boolean move(Point newPos) {
        return true; //this requires board awareness, so maybe not
    }



    void dealDamage(int damage) {
        currentHP-= damage;
        if (currentHP<0) currentHP = 0; //a bit unnecessary
    }
    void dealOOCDamage(int damage) {
        currentHP-= damage;

        if (currentHP<=0) currentHP = 1;
    }
    void heal(int health) {
        currentHP+= health;
        if (currentHP>getHP()) currentHP = getHP();
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