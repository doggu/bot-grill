package feh.battle.map;

import feh.characters.hero.MovementClass;

public enum Tile {
    PLAINS( "plains",
            true, true, true,
            false, false, false),
    PLAINS_D("plains",
            true, true, true,
            false, false, true),

    TRENCHES("trenches",
            true, true, true,
            false, true, false),
    TRENCHES_D("defensive trenches",
            true, true, true,
            false, true, true),

    FOREST( "forest",
            true, false, true,
            true, false, false),
    FOREST_D("defensive forest",
            true, false, true,
            true, false, true),

    MOUNTAIN("mountain",
            false, false, true,
            false, false, false),
    CLIFF(  "cliff",
            false, false, true,
            false, false, false),

    WATER(  "water",
            false, false, true,
            false, false, false),
    POND(   "pond",
            false, false, true,
            false, false, false),
    RIVER(  "river",
            false, false, true,
            false, false, false),
    LAVA(   "lava",
            false, false, true,
            false, false, false),

    WALL(   "wall",
            false, false, false,
            false, false, false);


    private final String name;
    private final boolean
            infantryPassable, cavalryPassable, flierPassable,
            slowsInfantry, slowsCavalry, defensiveTerrain;

    Tile(String name,
         boolean infantryPassable, boolean cavalryPassable, boolean flierPassable,
         boolean slowsInfantry, boolean slowsCavalry,
         boolean defensiveTerrain)
    {
        this.name = name;
        this.infantryPassable = infantryPassable;
        this.cavalryPassable = cavalryPassable;
        this.flierPassable = flierPassable;
        this.slowsInfantry = slowsInfantry;
        this.slowsCavalry = slowsCavalry;
        this.defensiveTerrain = defensiveTerrain;
    }


    String getName() { return name; }

    public boolean canPass(MovementClass moveType) {
        switch(moveType) {
            case INFANTRY:
            case ARMORED:
                return infantryPassable;
            case CAVALRY:
                return cavalryPassable;
            case FLYING:
                return flierPassable;
            default:
                System.out.println("I do not know what alien this is");
                throw new Error();
        }
    }

    public boolean isSlowed(MovementClass moveType) {
        switch(moveType) {
            case INFANTRY:
                return slowsInfantry;
            case ARMORED:
                return false; //fuck you armored units
            case CAVALRY:
                return slowsCavalry;
            case FLYING:
                return false; //they either can or cannot bruh
            default:
                System.out.println("I do not know what alien this is");
                throw new Error();
        }
    }

    public boolean isDefensiveTerrain() { return defensiveTerrain; }
}
