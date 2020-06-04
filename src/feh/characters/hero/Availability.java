/* future code
package feh.characters.hero;

import java.util.Date;

public class Availability {
public enum Type {
    NORMAL(true, true),
//        NORMAL_RARITY_CHANGED(true, true),
    SEASONAL(true, false),
    LEGENDARY(true, false),
    MYTHIC(true, false),
    STORY(false, false),
//        ENEMY(false, false),
    GHB(false, false),
    TT(false, false);

    private final boolean
            isSummonable,
            isInNormalPool;


    Type(boolean isSummonable, boolean isInNormalPool) {
        this.isSummonable = isSummonable;
        this.isInNormalPool = isInNormalPool;
    }

    public boolean isSummonable() { return isSummonable; }
    public boolean isInNormalPool() { return isInNormalPool; }
}



/**
 * determines whether or not the summonability of a unit represented by this
 * coincides in any way with the provided date
 *
 * @param date the date to compare against this's availability
 * @return an integer indicating star rating in the random pool
 *             3 representing 3-4*,
 *             4 representing 4-5*,
 *             5 representing 5* availability,
 *             -1 representing the unit not being available in the random
 *             pool.
 *
public int rarityInRandomPool(Date date) {
//compare to this unit's date ranges and
return -1;
}

public int rarityInRandomPool() {
    return rarityInRandomPool(new Date()); }
}
*/

package feh.characters.hero;

public enum Availability {
    NORMAL(true, true),
    NORMAL_RARITY_CHANGED(true, true),
    SEASONAL(true, false),
    LEGENDARY(true, false),
    MYTHIC(true, false),
    STORY(false, false),
    GHB(false, false),
    TT(false, false);

    private final boolean
            isSummonable,
            isInNormalPool;



    Availability(boolean isSummonable, boolean isInNormalPool) {
        this.isSummonable = isSummonable;
        this.isInNormalPool = isInNormalPool;
    }



    public boolean isSummonable() { return isSummonable; }
    public boolean isInNormalPool() { return isInNormalPool; }


}
