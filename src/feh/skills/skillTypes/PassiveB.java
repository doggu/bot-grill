package feh.skills.skillTypes;

import feh.skills.analysis.CooldownModifier;

import java.awt.*;

public class PassiveB extends Passive implements CooldownModifier {
    private final int cdModifier;



    public PassiveB(String name, String description, int cost, boolean exclusive, String icon) {
        super (name, description, new Color(0x003ED3), 'b', cost, exclusive, icon);

        //literally gotta do this just for Lunar Brace
        int cdModifier = 0;

        this.cdModifier = cdModifier;
    }



    @Override
    public int getCooldownModifier() {
        return cdModifier;
    }
}
