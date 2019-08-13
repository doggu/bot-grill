package feh.battle;

import feh.heroes.unit.Unit;

public class Combat {
    private final Unit a, d;
    public Combat(Unit initiator, Unit defeneder) {
        this.a = initiator;
        this.d = defeneder;
    }



    private static void attackPreview(FieldedUnit attacker, FieldedUnit defender) {
        FieldedUnit pAttack = attacker.clone();
        FieldedUnit pDefend = defender.clone();

        attack(pAttack, pDefend);


    }
    private static void attack(FieldedUnit attacker, FieldedUnit defender) {

    }
}
