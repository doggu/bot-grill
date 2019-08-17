package feh.battle;

import feh.heroes.UnitDatabase;
import feh.heroes.character.Hero;
import feh.heroes.unit.Unit;

import java.util.ArrayList;

public class Assist extends Action {
    /*
    an interaction between allies during a map; generally beneficiary.
        movement assist skills
        rallying
        harsh command
        healing (incl. reciprocal aid/ardent sacrifice/Sacrifice)
     */



    public Assist(FieldedUnit initiator, FieldedUnit receiver) {
        super(initiator, receiver);
    }



    public void commit() {

    }



    public static void main(String[] args) {
        ArrayList<Hero> heroes = UnitDatabase.HEROES;

        Unit maribelle, karla;

        for (Hero x:heroes) {

        }
    }
}
