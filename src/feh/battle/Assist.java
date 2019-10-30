package feh.battle;

import feh.characters.HeroDatabase;
import feh.characters.hero.Hero;
import feh.characters.unit.Unit;

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
        ArrayList<Hero> heroes = HeroDatabase.HEROES;

        Unit maribelle, karla;

        for (Hero x:heroes) {

        }
    }
}
