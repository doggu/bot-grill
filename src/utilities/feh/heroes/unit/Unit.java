package utilities.feh.heroes.unit;

import utilities.feh.heroes.character.Hero;

public class Unit extends Hero {
    private final int rarity, boon, bane; //I refuse to call it asset/flaw
    private final char supportStatus;
    private final int merges, dragonflowers;



    //TODO: create character class
    // how do i check things off 40%

    //TODO: implement dragonflowers (lol, if i even finish everything else that came before them)


    /**
     * creates a new unit based on a hero;
     * accepts IVs. NOTE: IVs must not be of the same stat
     *
     * @param hero - hero which corresponds with the summoned hero.
     * @param boon - positive variance of specific unit.
     * @param bane - negative variance of specific unit.
     */
    public Unit(Hero hero, int rarity, int boon, int bane, char supportStatus, int merges, int dragonflowers) {
        super(hero);
        this.rarity = rarity;
        this.boon = boon;
        this.bane = bane;
        this.supportStatus = supportStatus;
        this.merges = merges;
        this.dragonflowers = dragonflowers;
    }
    public Unit(Hero hero, int rarity, int boon, int bane, char supportStatus) {
        this(hero, rarity, boon, bane, supportStatus, 0, 0);
    }
    public Unit(Hero hero, int rarity, int boon, int bane, char supportStatus, int merges) {
        this(hero, rarity, boon, bane, supportStatus, merges, 0);
    }
    public Unit(Hero hero, int rarity, int boon, int bane) {
        this(hero, rarity, boon, bane, 'd');
    }

    public static final int
        HP = 0,
        ATK = 1,
        SPD = 2,
        DEF = 3,
        RES = 4;

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
        //damn this is fuckin retarded
        switch (individuality) {
            case 0: boon = -1; bane = -1; break;
            case 1: boon = HP; bane = ATK; break;
            case 2: boon = HP; bane = SPD; break;
            case 3: boon = HP; bane = DEF; break;
            case 4: boon = HP; bane = RES; break;
            case 5: boon = ATK; bane = HP; break;
            case 6: boon = ATK; bane = SPD; break;
            case 7: boon = ATK; bane = DEF; break;
            case 8: boon = ATK; bane = RES; break;
            case 9: boon = SPD; bane = HP; break;
            case 10: boon = SPD; bane = ATK; break;
            case 11: boon = SPD; bane = DEF; break;
            case 12: boon = SPD; bane = RES; break;
            case 13: boon = DEF; bane = HP; break;
            case 14: boon = DEF; bane = ATK; break;
            case 15: boon = DEF; bane = SPD; break;
            case 16: boon = DEF; bane = RES; break;
            case 17: boon = RES; bane = HP; break;
            case 18: boon = RES; bane = ATK; break;
            case 19: boon = RES; bane = SPD; break;
            case 20: boon = RES; bane = DEF; break;
            default:
                System.out.println("something hella broke");
                throw new Error();
        }

        this.boon = boon;
        this.bane = bane;
        supportStatus = 'd';
        this.merges = 0;
        this.dragonflowers = 0;
    }



    public int getBoon() { return boon; }
    public int getBane() { return bane; }
    public int getRarity() { return rarity; }
    public int getMerges() { return merges; }
    public int getDragonflowers() { return dragonflowers; }
    public char getSupportStatus() { return supportStatus; }
    public int[] getIVs() {
        int[] stats = super.getStats().getStatsAsArray();

        for (int i=0; i<stats.length; i++) {
            if (i==boon)
                stats[i]++;
            if (i==bane)
                stats[i]--;
        }

        return stats;
    }
    public int[] getStatsArr() {
        int[] stats = super.getStats(false, rarity, boon, bane);

        switch (supportStatus) {
            case 's':
                stats[0]++;
                stats[1]+= 2;
            case 'a':
                stats[2]+= 2;
            case 'b':
                stats[0]++;
                stats[3]+= 2;
            case 'c':
                stats[0]+= 3;
                stats[4]+= 2;
            case 'd':
                break;
            default:
                //technically shouldn't happen but it's okay I guess
                System.out.println("just letting you know that I didn't get a support status for "+this.getFullName());
                break;
        }

        return stats;
    }
}
