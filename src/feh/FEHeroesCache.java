package feh;

import utilities.data.WebCache;

public class FEHeroesCache extends WebCache {
    private static final String FEHEROES_SUBDIR = "feh";


    public FEHeroesCache(String wiki, String subdir) {
        super(wiki, FEHEROES_SUBDIR+(subdir!=null?subdir:""));
    }
    public FEHeroesCache(String wiki) {
        this(wiki, null);
    }
}
