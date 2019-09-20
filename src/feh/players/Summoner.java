package feh.players;

import events.fehGame.summoning.CircleSimulator;
import feh.heroes.character.Hero;
import feh.heroes.unit.Unit;
import feh.players.relationships.Relationship;
import feh.players.relationships.Relationships;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;

public class Summoner {
    public static final ArrayList<Summoner> SUMMONERS;

    static {
        SUMMONERS = new ArrayList<>();
        //todo: turn this into the source of persistence
    }



    private final User summoner;
    private final Barracks barracks;

    private Unit supportedUnit = null;
    private Relationships relationships = new Relationships();

    private int orbsSpent = 0;
    private boolean summoning;
    private CircleSimulator currentSession = null;



    //for generating summoners from a cache
    //public Summoner(User summoner, Barracks barracks) {}
    public Summoner(User summoner) {
        this.summoner = summoner;
        this.barracks = new Barracks();
        summoning = false;
    }



    public void setSupportedUnit(Unit partner) { this.supportedUnit = partner; }
    public boolean supportHeroes(Hero h1, Hero h2) {
        return relationships.add(new Relationship(h1, h2));
    }



    public String getName() {
        return summoner.getName();
    }
    public int getOrbsSpent() {
        return orbsSpent;
    }
    public User getUser() { return summoner; }
    public boolean isSummoning() { return summoning; }
    public CircleSimulator getCurrentSession() { return currentSession; }
    public Barracks getBarracks() { return barracks; }

    public Unit getSupportedUnit() {
        return supportedUnit;
    }
    public boolean isSupportedUnit(Unit s) {
        return s==supportedUnit;
    }
    public Relationships getRelationships() {
        return relationships;
    }



    public void startSummoning(CircleSimulator session) {
        summoning = true;
        currentSession = session;
    }
    public void stopSummoning() {
        summoning = false;
        currentSession = null;
    }
    public void spendOrbs(int orbs) {
        orbsSpent+= orbs;
    }
}
