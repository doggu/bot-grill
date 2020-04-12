package feh;

import utilities.data.WebCache;

public class FEHeroesCache extends WebCache {
    private static final String
            FEHEROES_URL = "https://feheroes.gamepedia.com/",
            FEHEROES_SUBDIR = "feh";


    //TODO: replace old utilities with new jsoupy ones
    public FEHeroesCache(String wiki, String subdir) {
        super(  FEHEROES_URL+wiki,
                FEHEROES_SUBDIR+(subdir!=null?subdir:""));
    }
    public FEHeroesCache(String wiki) {
        this(wiki, null);
    }
}
