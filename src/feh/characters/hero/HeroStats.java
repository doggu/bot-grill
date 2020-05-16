package feh.characters.hero;

public class HeroStats {
    //these are lv1, 5* and neutral
    private final int hp, atk, spd, def, res;
    //these are lv1, 3* and neutral
    private final int hpG, atkG, spdG, defG, resG;



    private HeroStats(int hp, int atk, int spd, int def, int res,
                      int hpG, int atkG, int spdG, int defG, int resG) {
        this.hp = hp;
        this.atk = atk;
        this.spd = spd;
        this.def = def;
        this.res = res;
        this.hpG = hpG;
        this.atkG = atkG;
        this.spdG = spdG;
        this.defG = defG;
        this.resG = resG;
    }
    public HeroStats(int[] stats, int[] growths) {
        this(stats[0], stats[1], stats[2], stats[3], stats[4],
                growths[0], growths[1], growths[2], growths[3], growths[4]);
    }



    public int getHp() { return hp; }
    public int getAtk() { return atk; }
    public int getSpd() { return spd; }
    public int getDef() { return def; }
    public int getRes() { return res; }
    //public int getHpGrowth() { return hpG; }
    //public int getAtkGrowth() { return atkG; }
    //public int getSpdGrowth() { return spdG; }
    //public int getDefGrowth() { return defG; }
    //public int getResGrowth() { return resG; }
    //public int getBST(boolean lv1, int boon, int bane) { return 69; }

    public int[] getGrowthsAsArray() {
        return new int[]{ this.hpG, this.atkG, this.spdG, this.defG, this.resG };
    }
    public int[] getStatsAsArray() {
        return new int[]{ this.hp, this.atk, this.spd, this.def, this.res };
    }
}
