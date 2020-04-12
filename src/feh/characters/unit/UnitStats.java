package feh.characters.unit;

import feh.characters.hero.HeroStats;

public class UnitStats {
    private final HeroStats character;
    private int merges, dragonflowers;
    private char supportStatus;


    public UnitStats(HeroStats character) {
        this(character,0,0,'d'); }
    public UnitStats(HeroStats character, int merges) {
        this(character, merges,0,'d'); }
    public UnitStats(HeroStats character, char supportStatus) {
        this(character, 0,0, supportStatus); }
    public UnitStats(HeroStats character, int merges,
                     int dragonflowers, char supportStatus) {
        this.character = character;
        this.merges = merges;
        this.dragonflowers = dragonflowers;
        this.supportStatus = supportStatus;
    }
}