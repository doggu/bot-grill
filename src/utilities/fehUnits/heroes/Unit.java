package utilities.fehUnits.heroes;

public class Unit extends Hero {
    private final int rarity, boon, bane;
    private final char supportStatus;

    //TODO: create character class


    /**
     * creates a new unit based on a hero;
     * accepts IVs. NOTE: IVs must not be of the same stat
     *
     * @param hero - hero which corresponds with the summoned hero.
     * @param boon - positive variance of specific unit.
     * @param bane - negative variance of specific unit.
     */
    public Unit(Hero hero, int rarity, int boon, int bane, char supportStatus) {
        super(hero);
        this.rarity = rarity;
        this.boon = boon;
        this.bane = bane;
        this.supportStatus = supportStatus;
    }
    public Unit(Hero hero, int rarity, int boon, int bane) {
        this(hero, rarity, boon, bane, 'd');
    }

    /**
     * creates a new unit based on a character;
     * generates IVs randomly
     * no summoner support, as this is a "new" instance (subject to change)
     *
     * @param unit - name of hero. must follow EXACT format:
     *             "[name]: [epithet]"
     * @param rarity - rarity of hero.
     */
    public Unit(Hero unit, int rarity) {
        super(unit);
        this.rarity = rarity;

        int boon, bane;

        int individuality = (int)(Math.random()*21);
        if (individuality==0) {
            //neutral
            boon = -1;
            bane = -1;
        } else {
            //damn this is fuckin retarded
            switch (individuality) {
                case 1: boon = 0; bane = 1; break;
                case 2: boon = 0; bane = 2; break;
                case 3: boon = 0; bane = 3; break;
                case 4: boon = 0; bane = 4; break;
                case 5: boon = 1; bane = 0; break;
                case 6: boon = 1; bane = 2; break;
                case 7: boon = 1; bane = 3; break;
                case 8: boon = 1; bane = 4; break;
                case 9: boon = 2; bane = 0; break;
                case 10: boon = 2; bane = 1; break;
                case 11: boon = 2; bane = 3; break;
                case 12: boon = 2; bane = 4; break;
                case 13: boon = 3; bane = 0; break;
                case 14: boon = 3; bane = 1; break;
                case 15: boon = 3; bane = 2; break;
                case 16: boon = 3; bane = 4; break;
                case 17: boon = 4; bane = 0; break;
                case 18: boon = 4; bane = 1; break;
                case 19: boon = 4; bane = 2; break;
                case 20: boon = 4; bane = 3; break;
                default:
                    System.out.println("something hella broke");
                    throw new Error();
            }
        }

        this.boon = boon;
        this.bane = bane;
        supportStatus = 'd';
    }

    public int getBoon() { return boon; }
    public int getBane() { return bane; }
    public int getRarity() { return rarity; }
    public int getSupportStatus() { return supportStatus; }

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
