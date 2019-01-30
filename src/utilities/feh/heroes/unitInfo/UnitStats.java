package utilities.feh.heroes.unitInfo;

import utilities.feh.heroes.character.HeroStats;

public class UnitStats {
    private final HeroStats character;
    private int merges;
    private char supportStatus;



    public UnitStats(HeroStats character) { this(character, 0, 'd'); }
    public UnitStats(HeroStats character, int merges) { this(character, merges, 'd'); }
    public UnitStats(HeroStats character, char supportStatus) { this(character, 0, supportStatus); }
    public UnitStats(HeroStats character, int merges, char supportStatus) {
        this.character = character;
        this.merges = merges;
        this.supportStatus = supportStatus;
    }


}

enum Stats {
    HP, ATK, SPD, DEF, RES
}

enum IV {

}