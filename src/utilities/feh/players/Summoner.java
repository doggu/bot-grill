package utilities.feh.players;

import net.dv8tion.jda.core.entities.User;

public class Summoner {
    private final User summoner;
    private int orbsSpent = 0;
    private boolean summoning;



    public Summoner(User summoner) {
        this.summoner = summoner;
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



    public void startSummoning() { summoning = true; }
    public void stopSummoning() { summoning = false; }
    public void spendOrbs(int orbs) {
        orbsSpent+= orbs;
    }
}
