package feh.players.relationships;

import feh.heroes.character.Hero;

import java.util.ArrayList;

public class Relationships extends ArrayList<Relationship> {
    public boolean add(Relationship f) {
        if (f.h1==f.h2) return false;
        if (contains(f.h1)||contains(f.h2)) return false;

        super.add(f);
        return true;
    }



    public Relationship findRelationship(Hero h) {
        for (Relationship r:this) {
            if (r.contains(h)) return r;
        }

        return null;
    }



    public boolean contains(Hero h) {
        for (Relationship f:this)
            if (f.contains(h))
                return true;

        return false;
    }
}
