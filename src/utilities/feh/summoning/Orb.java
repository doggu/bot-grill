package utilities.feh.summoning;

import utilities.feh.heroes.Hero;
import utilities.feh.heroes.Unit;

public class Orb {
    private final Banner banner;
    private final Unit hero;
    private boolean isPulled;



    public Orb(Banner banner) {
        this.banner = banner;
        hero = generateUnit();
        isPulled = false;
    }



    private Unit generateUnit() {
        //TODO: actually like, make (Hero, IVs, correct summoning rates)
        Unit hero;

        Hero character;
        double r = Math.random()*100;
        int rarity;
        double  rarityFRate = banner.getRarityFRate(),
                rarity5Rate = rarityFRate+banner.getRarity5Rate(),
                rarity4Rate = rarity5Rate+banner.getRarity4Rate()/*,
                rarity3Rate = rarity4Rate+banner.getRarity3Rate()*/; //not necessary due to being the leftover category

        if (r<banner.getRarityFRate()) {
            rarity = 5;
            character = banner.getRarityFPool().get((int)(Math.random()*banner.getRarityFPool().size()));
            //System.out.println(banner.getRarityFPool().size());
        } else if (r<rarity5Rate) {
            rarity = 5;
            character = banner.getRarity5Pool().get((int)(Math.random()*banner.getRarity5Pool().size()));
            //System.out.println(banner.getRarity5Pool().size());
        } else if (r<rarity4Rate) {
            rarity = 4;
            character = banner.getRarity4Pool().get((int)(Math.random()*banner.getRarity4Pool().size()));
            //System.out.println(banner.getRarity4Pool().size());
        } else {
            rarity = 3;
            character = banner.getRarity3Pool().get((int)(Math.random()*banner.getRarity3Pool().size()));
            //System.out.println(banner.getRarity3Pool().size());
        }

        //Unit class generates random IVs for us TODO: might wanna reorganize this
        hero = new Unit(character, rarity);

        return hero;
    }

    public Unit pull() throws Exception {
        if (isPulled()) throw new Exception();
        isPulled = true;
        return hero;
    }

    public boolean isPulled() { return isPulled; }

    public String getColor() { return hero.getColor(); }
}
