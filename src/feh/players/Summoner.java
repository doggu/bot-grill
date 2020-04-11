package feh.players;

import events.fehGame.summoning.CircleSimulator;
import feh.characters.hero.Hero;
import feh.characters.unit.Unit;
import feh.players.relationships.Relationship;
import feh.players.relationships.Relationships;
import main.BotMain;
import net.dv8tion.jda.api.entities.User;

import java.io.*;
import java.util.ArrayList;

public class Summoner {
    public static ArrayList<Summoner> SUMMONERS;

    static {
        SUMMONERS = new ArrayList<>();
        //todo: turn this into the source of persistence
    }

    public static void register(String userId) {
        Summoner s = new Summoner(userId);
        SUMMONERS.add(s);
    }



    private final String userId;
    private final Barracks barracks;

    private Unit supportedUnit = null;
    private Relationships relationships = new Relationships();

    private int orbsSpent = 0;
    private boolean summoning;
    private CircleSimulator currentSession = null;



    public Summoner(String userId) {
        this.userId = userId;
        this.barracks = new Barracks();
        summoning = false;
    }

    //for generating summoners from a cache
    //public Summoner(User summoner, Barracks barracks) {}



    public void setSupportedUnit(Unit partner) { this.supportedUnit = partner; }
    public boolean supportHeroes(Hero h1, Hero h2) {
        return relationships.add(new Relationship(h1, h2));
    }



    public String getName() {
        return getUser().getName();
    }
    public int getOrbsSpent() {
        return orbsSpent;
    }
    public String getUserById() { return userId; }

    public User getUser() {
        return BotMain.bot_grill.retrieveUserById(userId).complete();
    }
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



    public static void main(String[] args) {
        serialize();
    }

    private static void serialize() {
        File f = new File("./src/feh/players/barracks.txt");

        try {
            if (f.createNewFile()) {
                System.out.println("created a new file");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }

        for (int i=0; i<5; i++) {
            SUMMONERS.add(new Summoner("heh"));
        }

        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(SUMMONERS);

            System.out.println("serialized");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static void deserialize() {
        File f = new File("./src/feh/players/barracks.txt");

        try {
            if (f.createNewFile()) {
                System.out.println("created a new file");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }

        try {
            FileInputStream fos = new FileInputStream(f);
            ObjectInputStream oos = new ObjectInputStream(fos);

            Object o = oos.readObject();

            if (o instanceof ArrayList)
                SUMMONERS = (ArrayList) o;

            System.out.println("serialized");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
