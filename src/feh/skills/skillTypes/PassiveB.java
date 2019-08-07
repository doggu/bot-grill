package feh.skills.skillTypes;

import feh.skills.analysis.CooldownModifier;

public class PassiveB extends Passive implements CooldownModifier {
    private final int cdModifier;



    public PassiveB(String name, String description, int cost, boolean exclusive) {
        super (name, description, 'b', cost, exclusive);

        //literally gotta do this just for Lunar Brace
        int cdModifier = 0;

        this.cdModifier = cdModifier;
    }



    @Override
    public int getCooldownModifier() {
        return cdModifier;
    }
}
