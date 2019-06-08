package feh.heroes.character;

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