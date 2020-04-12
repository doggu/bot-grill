package feh.players.relationships;

import feh.battle.Battle;
import feh.characters.hero.Hero;

import java.util.ArrayList;

public class Relationship {
    protected final Hero h1, h2;
    //a bit excessive but at least it's comprehensive and foolproof (for myself)
    private ArrayList<Battle> maps = new ArrayList<>();

    public Relationship(Hero h1, Hero h2) {
        //being handled in Relationships
//        if (h1==h2) throw new CannotBangSelfException();
        this.h1 = h1;
        this.h2 = h2;
    }



    public boolean addMap(Battle b) {
        if (!maps.contains(b)) {
            maps.add(b);
            return true;
        }

        return false;
    }



    public boolean contains(Hero h) {
        return h==h1||h==h2;
    }
}
