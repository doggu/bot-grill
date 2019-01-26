package utilities.feh.players;

import net.dv8tion.jda.core.entities.User;

public class Summoner {
    private final User summoner;
    private int orbsSpent = 0;



    public Summoner(User summoner) {
        this.summoner = summoner;
    }



    public String getName() {
        return summoner.getName();
    }
    public int getOrbsSpent() {
        return orbsSpent;
    }
    public User getUser() {
        return summoner;
    }



    public void spendOrbs(int orbs) {
        orbsSpent+= orbs;
    }
}
