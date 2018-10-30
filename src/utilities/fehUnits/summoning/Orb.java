package utilities.fehUnits.summoning;

import net.dv8tion.jda.core.entities.Emote;
import utilities.fehUnits.heroes.Character;
import utilities.fehUnits.heroes.Unit;
import utilities.fehUnits.heroes.UnitDatabase;

import java.awt.*;

public class Orb {
    private final Banner banner;
    private final Unit hero;
    private boolean isPulled;

    public Orb(Banner banner) {
        //TODO: must generate unit based on banner's pools

        //generate unit

        this.banner = banner;
        hero = generateUnit();
        isPulled = false;
    }

    private Unit generateUnit() {
        //TODO: actually like, make (Character, IVs)
        Unit hero;

        Character character;
        double r = Math.random();
        int rarity;
        if (r<0.03) {
            rarity = 5;
            character = banner.getRarityFPool().get((int)(Math.random()*banner.getRarityFPool().size()));
            System.out.println(banner.getRarityFPool().size());
        } else if (r<0.06) {
            rarity = 5;
            character = banner.getRarity5Pool().get((int)(Math.random()*banner.getRarity5Pool().size()));
            System.out.println(banner.getRarity5Pool().size());
        } else if (r<0.50) {
            rarity = 4;
            character = banner.getRarity4Pool().get((int)(Math.random()*banner.getRarity4Pool().size()));
            System.out.println(banner.getRarity4Pool().size());
        } else {
            rarity = 3;
            character = banner.getRarity3Pool().get((int)(Math.random()*banner.getRarity3Pool().size()));
            System.out.println(banner.getRarity3Pool().size());
        }

        hero = new Unit(character, rarity,2, 0);

        return hero;
    }

    public Unit pull() throws Exception {
        if (isPulled()) throw new Exception();
        isPulled = true;
        return hero;
    }

    public boolean isPulled() { return isPulled; }

    public String getColor() { return hero.getColor(); }

    public static void main(String[] args) {
        System.out.println(Color.black);
    }
}
