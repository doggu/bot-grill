package utilities.feh.players;

import events.fehGame.CircleSimulator;
import net.dv8tion.jda.core.entities.User;

public class Summoner {
    private final User summoner;
    private final Barracks barracks;
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
