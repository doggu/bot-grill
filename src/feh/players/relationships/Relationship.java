package feh.players.relationships;

import feh.heroes.character.Hero;

public class Relationship {
    protected final Hero h1, h2;
    private int exp = 0;

    public Relationship(Hero h1, Hero h2) {
        //if (h1==h2) throw new CannotBangSelfException(); being handled in Relationships
        this.h1 = h1;
        this.h2 = h2;
    }



    public boolean contains(Hero h) {
        return h==h1||h==h2;
    }
}
