package utilities.fehUnits.heroes;

import java.util.GregorianCalendar;

public class Unit extends Character {
    private final int rarity, boon, bane;

    //TODO: create character class

    /**
     * creates a new unit based on a character;
     * accepts IVs. NOTE: IVs must not be of the same stat
     *
     * @param unit - character which corresponds with the summoned hero.
     * @param boon - positive variance of specific unit.
     * @param bane - negative variance of specific unit.
     */
    public Unit(Character unit, int rarity, int boon, int bane) {
        super(unit);
        this.rarity = rarity;
        this.boon = boon;
        this.bane = bane;
    }

    /**
     * creates a new unit based on a character;
     * generates IVs randomly
     *
     * @param name - name of hero. must follow EXACT format:
     *             "[name]: [epithet]"
     */
    public Unit(String name, int rarity) {
        super(name);
        this.rarity = rarity;

        int boon, bane;

        int individuality = (int)(Math.random()*21);
        if (individuality==0) {
            //neutral
        } else {
            //damn this is fuckin retarded
            switch (individuality/5) {
                case 1:
                    bane = 0; boon = 1; break;
                case 2:
                    bane = 2; boon = 1; break;
                case 3:
                    bane = 3; boon = 1; break;
                case 4:
                    bane = 4; boon = 1; break;
                case 5:
                    bane = 5; boon = 1; break;
                case 6:
                    bane = 0; boon = 2; break;
                case 7:
                    bane = 1; boon = 2; break;
                case 8:
                    bane = 3; boon = 2; break;
                case 9:
                    bane = 4; boon = 2; break;
                case 10:
                    bane = 5; boon = 2; break;
                case 11:
                    bane = 0; boon = 3; break;
                case 12:
                    bane = 1; boon = 3; break;
                case 13:
                    bane = 2; boon = 3; break;
                case 14:
                    bane = 4; boon = 3; break;
                case 15:
                    bane = 5; boon = 3; break;
                case 16:
                    bane = 0; boon = 4; break;
                case 17:
                    bane = 1; boon = 4; break;
                case 18:
                    bane = 2; boon = 4; break;
                case 19:
                    bane = 3; boon = 4; break;
                case 20:
                    bane = 5; boon = 4; break;
                default:
                    System.out.println("something hella broke");
                    throw new Error();
            }
        }






















        this.boon = 5;
        this.bane = 3;
    }

    public int getBoon() { return boon; }
    public int getBane() { return bane; }
    public int getRarity() { return rarity; }

    public int[] getIVs() {
        int[] stats = super.getStats();

        for (int i=0; i<stats.length; i++) {
            if (i==boon)
                stats[i]++;
            if (i==bane)
                stats[i]--;
        }

        return stats;
    }

    public int[] getStats() {
        return super.getStats(false, rarity, boon, bane);
    }
}
